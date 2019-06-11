package com.imcco.sell.service;

import com.imcco.sell.dto.OrderDTO;
import com.lly835.bestpay.model.PayResponse;

public interface PayService {

    void create(OrderDTO orderDTO);


}
