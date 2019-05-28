package com.imcco.sell.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 买家信息
 */
@Entity
@Data
@DynamicUpdate
public class SellerInfo {

    @Id
    private String sellerId;//买家主键

    private String username;//用户名

    private String password;//用户密码

    private String openid;//微信openid

}
