package com.imcco.sell.service.impl;

import com.imcco.sell.dto.OrderDTO;
import com.imcco.sell.enums.ResultEnum;
import com.imcco.sell.exception.SellException;
import com.imcco.sell.service.BuyerService;
import com.imcco.sell.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BuyerServiceImpl implements BuyerService {

    @Autowired
    private OrderService orderService;

    @Override
    public OrderDTO findOrderOne(String openid, String orderId) {
        return checkOrderOwner(openid,orderId);
    }

    @Override
    public OrderDTO cancelOrder(String openid, String orderId) {
        OrderDTO orderDTO =checkOrderOwner(openid,orderId);
        if(orderDTO==null){
            log.error("【取消订单】查找不到该订单");
            throw  new  SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        return orderDTO;
    }

    private  OrderDTO checkOrderOwner(String openid, String orderId){
        OrderDTO orderDTO = orderService.findOne(orderId);
        //先判断这个订单是否存在
        if(orderDTO==null){
            return  null;
        }
        //再判断是不是这个人的订单
        if(!orderDTO.getBuyerOpenid().equalsIgnoreCase(openid)){
            log.error("【查询订单】订单的openid不对,openid={},orderDTO={}",openid,orderDTO);
            throw  new SellException(ResultEnum.ORDER_OWNER_ERROR);
        }

        return orderDTO;
    }
}
