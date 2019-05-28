package com.imcco.sell.dataobject;

import com.imcco.sell.enums.OrderSatutsEnum;
import com.imcco.sell.enums.PayStatusEnum;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * 订单主表
 */
@Entity
@Data
@DynamicUpdate
public class OrderMaster {
    @Id
    private String orderId;//主键

    private String buyerName;//买家名字

    private  String buyerPhoto;//买家电话

    private  String buyerAddress;//买家地址

    private  String buyerOpenId;//买家openid

    private BigDecimal orderAmount;//订单总金额

    private Integer orderStauts= OrderSatutsEnum.NEW.getCode();//订单状态 0为新下单

    private Integer payStatus= PayStatusEnum.WAIT.getCode();//支付状态 0为未支付



}
