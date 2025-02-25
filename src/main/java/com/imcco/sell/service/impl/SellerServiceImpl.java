package com.imcco.sell.service.impl;

import com.imcco.sell.dataobject.SellerInfo;
import com.imcco.sell.repository.SellerInfoRepository;
import com.imcco.sell.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerInfoRepository repository;

    @Override
    public SellerInfo findSellerInfoByOpenid(String openid) {

        return repository.findByOpenid(openid);
    }
}
