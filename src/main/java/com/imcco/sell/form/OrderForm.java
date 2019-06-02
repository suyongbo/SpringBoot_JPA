package com.imcco.sell.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 对应买家前端传过来的表单
 */
@Data
public class OrderForm {

    /**
     * 买家姓名
     */
    @NotEmpty(message = "姓名必填")
    private  String name;
    /**
     * 买家电话
     */
    @NotEmpty(message = "电话必填")
    private  String phone;
    /**
     * 买家地址
     */
    @NotEmpty(message = "地址必填")
    private  String address;
    /**
     * 买家微信openid
     */
    @NotEmpty(message = "买家openid不能为空")
    private  String openid;
    /**
     * 购物车
     */
    @NotEmpty(message = "购物车不为空")
    private  String items;
}
