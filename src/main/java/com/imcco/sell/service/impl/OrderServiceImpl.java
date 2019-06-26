package com.imcco.sell.service.impl;

import com.imcco.sell.converter.OrderMaster2OrderConverter;
import com.imcco.sell.dataobject.OrderDetail;
import com.imcco.sell.dataobject.OrderMaster;
import com.imcco.sell.dataobject.ProductInfo;
import com.imcco.sell.dto.CartDTO;
import com.imcco.sell.dto.OrderDTO;
import com.imcco.sell.enums.OrderStatusEnum;
import com.imcco.sell.enums.PayStatusEnum;
import com.imcco.sell.enums.ResultEnum;
import com.imcco.sell.exception.SellException;
import com.imcco.sell.repository.OrderDetailRepository;
import com.imcco.sell.repository.OrderMasterRepository;
import com.imcco.sell.service.*;
import com.imcco.sell.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 订单
 */
@Service
@Slf4j
public class OrderServiceImpl  implements OrderService {

    @Autowired
    private ProductInfoService productInfoService;//商品信息

    @Autowired
    private OrderDetailRepository orderDetailRepository;//订单详情DAO

    @Autowired
    private OrderMasterRepository orderMasterRepository;//订单主表DAO

    @Autowired
    private PayService payService;

    //微信
    @Autowired
    private  PushMessageService pushMessageService;

    //客户
    @Autowired
    private WebSocket webSocket;

    /**
     * 创建订单  注意 只在有新的订单时客户端有消息推送
     * @param orderDTO
     * @return
     */
    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO){

        //当订单一创建 就先产生订单的orderId
        String orderId = KeyUtil.genUniqueKey();

        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);//先创建一个订单总价


        //1.查询商品(数量，单价)
        for(OrderDetail orderDetail :orderDTO.getOrderDetailList()){//得到下单的所有订单详情
            ProductInfo productInfo = productInfoService.findOne(orderDetail.getProductId());
            if(productInfo==null){//判断商品信息是否为空
                throw  new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            //2.计算订单总价
            //用数据库的商品单价 * 传过来的详情里面的购买数量  然后不断的add到外面的订单总价的这个变量
            orderAmount = productInfo.getProductPrice().
                    multiply(new BigDecimal(orderDetail.getProductQuantity()))
                    .add(orderAmount);
            //3.订单详情入库
            //因为数据库 orderId  detailId是varchar 所以这里需要随机一个数 常见一个工具类来产生
            orderDetail.setDetailId(KeyUtil.genUniqueKey());
            orderDetail.setOrderId(orderId);
            //这里需要注意用到 copy这个东西的时候 有覆盖的作用  需要注意复制的顺序  先
            BeanUtils.copyProperties(productInfo,orderDetail);
            orderDetailRepository.save(orderDetail);//将新的orderdetail重新save

        }
        //4.写入订单数据库(OrderMsater OrderDetail)
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO,orderMaster);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterRepository.save(orderMaster);
        //5.减库存
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(e ->
                new CartDTO(e.getProductId(),e.getProductQuantity())).collect(Collectors.toList());
        productInfoService.decreaseStock(cartDTOList);

        //发送webSocket消息
        webSocket.sendMessage(orderDTO.getOrderId());

        return orderDTO;
    }

    /**
     * 查询单个订单
     * @param orderId
     * @return
     */
    @Override
    public OrderDTO findOne(String orderId) {

        Optional<OrderMaster> orderMaster1 = orderMasterRepository.findById(orderId);
        OrderMaster orderMaster = orderMaster1.get();
       // OrderMaster orderMaster =orderMasterRepository.findById(orderId).get();
        //判断主订单信息
        if(orderMaster==null){
            throw  new  SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        //判断订单详情
        if(CollectionUtils.isEmpty(orderDetailList)){
            throw  new  SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster,orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);

        return orderDTO;
    }

    /**
     * 分页查询
     * @param buyerOpenId
     * @return
     */
    @Override
    public Page<OrderDTO> findList(String buyerOpenId,Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findByBuyerOpenid(buyerOpenId,pageable);
        //orderMasterPage.getContent() 返回的是OrderMaster
        List<OrderDTO> orderDTOList = OrderMaster2OrderConverter.convert(orderMasterPage.getContent());
        //返回的是 Page<OrderDTO>
        return new PageImpl<OrderDTO>(orderDTOList,pageable,orderMasterPage.getTotalElements());
    }

    /**
     * 取消订单
     * @param orderDTO
     * @return
     */
    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
        OrderMaster orderMaster =  new OrderMaster();
        //1.判断订单状态  如果不是新订单就抛出异常
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【取消订单】订单状态不正确 orderId={},orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw  new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //2.修改订单状态  修改的是订单主表 OrderMaster
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        //看订单是否修改成功
        if(updateResult==null){
            log.error("【取消订单】更新失败，orderMaster={}",orderMaster);
            throw  new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        //3.返回库存  返回的是商品表 ProductInfo
            //判断订单详情中是否有商品  CollectionUtils.isEmpty(null): true  判断集合是否为空
        if(CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){
            log.error("【取消订单】订单中无商品详情，orderDTO={}",orderDTO);
            throw  new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(e->
                new CartDTO(e.getProductId(),e.getProductQuantity())).collect(Collectors.toList());

        productInfoService.increaseStock(cartDTOList);
        //4.如果已经支付，需要退款
        if(orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())){
                payService.refund(orderDTO);
        }
        return orderDTO;
    }

    /**
     * 完结订单  注意这里只有在完结订单时才向微信推送消息
     * @param orderDTO
     * @return
     */
    @Override
    @Transactional
    public OrderDTO finish(OrderDTO orderDTO) {
        //判断订单状态
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【完结订单】订单状态不正确 orderId={},orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw  new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster orderMaster= new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if(updateResult==null){
            log.error("【完结订单】更新失败，orderMaster={}",orderMaster);
            throw  new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        //推送微信模板消息
        pushMessageService.orderStatus(orderDTO);

        return orderDTO;
    }

    /**
     * 支付订单
     * @param orderDTO
     * @return
     */
    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {

        //判断订单状态
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【订单支付完成】订单状态不正确 orderId={},orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw  new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //判断订单支付状态
        if(!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())){
            log.error("【订单支付完成】订单支付状态不正确 orderDTO={}",orderDTO);
            throw  new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }
        //修改订单状态
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster= new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if(updateResult==null){
            log.error("【订单支付完成】更新失败，orderMaster={}",orderMaster);
            throw  new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(Pageable pageable) {
       Page<OrderMaster> orderMasterPage = orderMasterRepository.findAll(pageable);
        List<OrderDTO> orderDTOList = OrderMaster2OrderConverter.convert(orderMasterPage.getContent());
        //返回的是 Page<OrderDTO>
        return new PageImpl<OrderDTO>(orderDTOList,pageable,orderMasterPage.getTotalElements());
    }
}
