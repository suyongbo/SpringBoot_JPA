package com.imcco.sell.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import java.math.BigDecimal;

/**
 * 商品详情
 */
@Entity
@Data
public class ProductInfo {

    private  String productId;//主键

    private  String productName;//商品名称

    private  BigDecimal productPrice;//商品单价

    private Integer productStock;//商品库存

    private  String productDescription;//商品描述

    private  String productIcon;//商品小图

    private  Integer productType;//商品类型

    private  Integer productStatus;//商品状态 0为上架 1为下架

    private  Integer categoryType;//对应类目表的编号


}
