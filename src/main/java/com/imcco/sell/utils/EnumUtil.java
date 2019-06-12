package com.imcco.sell.utils;

import com.imcco.sell.enums.CodeEnum;

/**
 * 传入对应的枚举类 和code  循环判断 找出对应的状态 返回对应的msg
 */
public class EnumUtil {
    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumClass) {
        for (T each: enumClass.getEnumConstants()) {
            if (code.equals(each.getCode())) {
                return each;
            }
        }
        return null;
    }
}
