package com.imcco.sell.enums;

import lombok.Getter;

/**
 * 订单状态
 */
@Getter
public enum OrderSatutsEnum implements CodeEnum {
    NEW(0, "新订单"),
    FINISHED(1, "完结"),
    CANCEL(2, "已取消"),
    ;
    private Integer code;

    private String message;

    OrderSatutsEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
