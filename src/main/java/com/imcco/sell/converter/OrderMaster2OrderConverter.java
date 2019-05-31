package com.imcco.sell.converter;

import com.imcco.sell.dataobject.OrderMaster;
import com.imcco.sell.dto.OrderDTO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 转换器 OrderMaster-->OrderDTO
 */
public class OrderMaster2OrderConverter {

    public  static OrderDTO convert(OrderMaster orderMaster){

        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster,orderDTO);

        return orderDTO;
    }

    public  static List<OrderDTO> convert(List<OrderMaster> orderMasterList){

        return orderMasterList.stream().map(e->convert(e)).collect(Collectors.toList());
    }
}
