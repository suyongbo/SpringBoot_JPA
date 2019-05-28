package com.imcco.sell.repository;

import com.imcco.sell.dataobject.SellerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerInfoRepository extends JpaRepository<SellerInfo, String > {
    SellerInfo findByOpenid(String openid);
}
