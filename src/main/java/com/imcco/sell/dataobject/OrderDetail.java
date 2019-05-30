package com.imcco.sell.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 订单详情
 */
@Entity
@Data
@DynamicUpdate
public class OrderDetail {
    @Id
    private  String datailId;

    private  String orderId;

    private  String productId;

    private  String productName;//商品名称

    private  String productPrice;//商品单价

    private  String productQuantity;//商品数量

    private  String productIcon;//商品小图


}
