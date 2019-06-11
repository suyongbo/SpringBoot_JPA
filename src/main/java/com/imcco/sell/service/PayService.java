package com.imcco.sell.service;

import com.imcco.sell.dto.OrderDTO;
import com.lly835.bestpay.model.PayResponse;

public interface PayService {
    //微信支付
    PayResponse create(OrderDTO orderDTO);
    //微信异步通知
    PayResponse notify(String notifyData);
    //微信退款
    PayResponse refund(OrderDTO orderDTO);
}
