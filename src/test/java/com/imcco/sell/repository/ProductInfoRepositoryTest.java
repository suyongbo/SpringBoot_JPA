package com.imcco.sell.repository;

import com.imcco.sell.dataobject.ProductInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class ProductInfoRepositoryTest {

    @Autowired
    private  ProductInfoRepository repository;

    @Test
    public  void save(){
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId("123");
        productInfo.setCategoryType(2);
        productInfo.setProductName("皮皮虾");
        productInfo.setProductDescription("香辣可口");
        productInfo.setProductPrice(new BigDecimal(3.2));
        productInfo.setProductStatus(0);
    }
}