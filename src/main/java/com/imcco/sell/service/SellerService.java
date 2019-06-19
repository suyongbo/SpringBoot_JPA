package com.imcco.sell.service;

import com.imcco.sell.dataobject.SellerInfo;

/**
 * 卖家端
 */
public interface SellerService {

    SellerInfo findSellerInfoByOpenid(String openid);
}
