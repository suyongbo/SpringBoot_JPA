package com.imcco.sell.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.imcco.sell.dataobject.OrderDetail;
import com.imcco.sell.enums.OrderStatusEnum;
import com.imcco.sell.enums.PayStatusEnum;
import com.imcco.sell.utils.EnumUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDTO {
    private String orderId;//主键

    private String buyerName;//买家名字

    private  String buyerPhone;//买家电话

    private  String buyerAddress;//买家地址

    private  String buyerOpenid;//买家openid

    private BigDecimal orderAmount;//订单总金额

    private Integer orderStatus= OrderStatusEnum.NEW.getCode();//订单状态 0为新下单

    private Integer payStatus= PayStatusEnum.WAIT.getCode();//支付状态 0为未支付

    List<OrderDetail> orderDetailList;

    //FTL中 把订单状态支付状态的数字显示为中文汉字  不用在ftl这中if判断
    @JsonIgnore  //这个注解表示返回OrderDTO对象不包含被注解的属性
    public  OrderStatusEnum getOrderStatusEnum(){
        return EnumUtil.getByCode(orderStatus,OrderStatusEnum.class);
    }
    @JsonIgnore
    public  PayStatusEnum getPayStatusEnum(){
        return EnumUtil.getByCode(payStatus,PayStatusEnum.class);
    }
}
