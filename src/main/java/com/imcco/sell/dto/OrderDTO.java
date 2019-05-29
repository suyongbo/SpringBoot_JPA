package com.imcco.sell.dto;

import com.imcco.sell.dataobject.OrderDetail;
import com.imcco.sell.enums.OrderSatutsEnum;
import com.imcco.sell.enums.PayStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDTO {
    private String orderId;//主键

    private String buyerName;//买家名字

    private  String buyerPhoto;//买家电话

    private  String buyerAddress;//买家地址

    private  String buyerOpenId;//买家openid

    private BigDecimal orderAmount;//订单总金额

    private Integer orderStauts= OrderSatutsEnum.NEW.getCode();//订单状态 0为新下单

    private Integer payStatus= PayStatusEnum.WAIT.getCode();//支付状态 0为未支付

    List<OrderDetail> orderDetailList;
}
