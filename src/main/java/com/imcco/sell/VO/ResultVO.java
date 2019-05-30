package com.imcco.sell.VO;

import lombok.Data;

/**
 * http请求返回的最外层对象
 */
@Data
public class ResultVO<T> {

    private  Integer code;//返回到前端的错误码

    private  String msg;//返回到前端的提示信息

    private  T data;//具体内容
}
