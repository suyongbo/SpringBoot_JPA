package com.imcco.sell.service;

import com.imcco.sell.dto.OrderDTO;

/**
 * 因为有些判断需要做
 * 所以把查询一个 和取消订单 的部分业务用在这里判断
 * 而不是在控制层
 */
public interface BuyerService {
    //查询一个订单
    OrderDTO findOrderOne(String openid,String orderId);
    //取消订单
    OrderDTO cancelOrder(String openid,String orderId);
}
