package com.imcco.sell.service.impl;

import com.imcco.sell.dataobject.SellerInfo;
import com.imcco.sell.service.SellerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SellerServiceImplTest {

    private static  final  String openid="abc";

    @Autowired
    private SellerService sellerService;

    @Test
    public void findSellerInfoByOpenid(){
     SellerInfo sellerInfo= sellerService.findSellerInfoByOpenid(openid);
        Assert.assertNotNull(sellerInfo);
    }
}