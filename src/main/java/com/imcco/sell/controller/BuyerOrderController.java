package com.imcco.sell.controller;

import com.imcco.sell.VO.ResultVO;
import com.imcco.sell.converter.OrderForm2OrderDTOConverter;
import com.imcco.sell.dto.OrderDTO;
import com.imcco.sell.enums.ResultEnum;
import com.imcco.sell.exception.SellException;
import com.imcco.sell.form.OrderForm;
import com.imcco.sell.service.BuyerService;
import com.imcco.sell.service.OrderService;
import com.imcco.sell.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BuyerService buyerService;

    //创建订单
    @PostMapping("/create")
    public ResultVO<Map<String,String>> create(@Valid OrderForm orderForm,
                                            BindingResult bindingResult){
        //这里的bindingResult 指就是orderForm
        //根据valid里面的验证 看看有没有错误
        if(bindingResult.hasErrors()){
            log.error("【创建订单】参数不正确，orderForm",orderForm);
            throw  new SellException(ResultEnum.PARAM_ERROR.getCode(),bindingResult.getFieldError().getDefaultMessage());
            //后面的是返回验证里面的message
        }
        OrderDTO orderDTO = OrderForm2OrderDTOConverter.convert(orderForm);
        //判断购物车是否为空
        if(CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){
            log.error("【创建订单】购物车不能为空");
            throw  new SellException(ResultEnum.CART_EMPTY);
        }
        OrderDTO createResult =orderService.create(orderDTO);
        Map<String,String> map = new HashMap<>();
        map.put("orderId",createResult.getOrderId());
        return ResultVOUtil.success(map);
    }

    //订单列表
    @GetMapping("/list")
    public  ResultVO<List<OrderDTO>> list(@RequestParam("openid") String openid,
                                          @RequestParam(value = "page",defaultValue = "0") Integer page,
                                          @RequestParam(value = "size",defaultValue = "10") Integer size){
        if(StringUtils.isEmpty(openid)){
            log.error("【查询订单列表】openid为空");
            throw  new SellException(ResultEnum.PARAM_ERROR);
        }
        PageRequest request = new PageRequest(page,size);
        Page<OrderDTO> orderDTOPage = orderService.findList(openid,request);
        //  getContent() 获取流，查API说是获取内容模式
        return ResultVOUtil.success(orderDTOPage.getContent());
    }

    //订单详情
    @GetMapping("/detail")
    public  ResultVO<OrderDTO> detail(@RequestParam("openid") String openid,
                                      @RequestParam("orderId") String orderId){
        OrderDTO orderDTO =buyerService.findOrderOne(openid,orderId);
        return ResultVOUtil.success(orderDTO);
    }
    //取消订单
    @PostMapping("/cancel")
    public  ResultVO<OrderDTO> cancel(@RequestParam("openid") String openid,
                                      @RequestParam("orderId") String orderId){
        OrderDTO orderDTO1=  buyerService.cancelOrder(openid,orderId);
        OrderDTO orderDTO = orderService.cancel(orderDTO1);
        if(orderDTO==null){
            log.error("【取消订单】改订单不存在");
            throw  new  SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        return  ResultVOUtil.success(orderDTO);
    }
}
