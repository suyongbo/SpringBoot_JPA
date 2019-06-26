package com.imcco.sell.service;

import com.imcco.sell.dto.OrderDTO;

/**
 * 微信端消息推送（订单状态更新通知）
 */
public interface PushMessageService {

    void orderStatus(OrderDTO orderDTO);
}
