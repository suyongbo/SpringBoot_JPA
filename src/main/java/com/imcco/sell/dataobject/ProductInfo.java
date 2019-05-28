package com.imcco.sell.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.imcco.sell.enums.ProductStatusEnum;
import com.imcco.sell.utils.EnumUtil;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * 商品详情
 */
@Entity
@Data
@DynamicUpdate
public class ProductInfo {

    @Id
    private  String productId;//主键

    private  String productName;//商品名称

    private  BigDecimal productPrice;//商品单价

    private Integer productStock;//商品库存

    private  String productDescription;//商品描述

    private  String productIcon;//商品小图

    private  Integer productType;//商品类型

    private  Integer productStatus= ProductStatusEnum.UP.getCode();//商品状态 0为上架 1为下架

    private  Integer categoryType;//对应类目表的编号

    @JsonIgnore
    public ProductStatusEnum getProductStatusEnum() {
        return EnumUtil.getByCode(productStatus, ProductStatusEnum.class);
    }


}
