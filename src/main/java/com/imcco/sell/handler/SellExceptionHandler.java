package com.imcco.sell.handler;

import com.imcco.sell.config.ProjectUrlConfig;
import com.imcco.sell.exception.SellerAuthorizeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * 增强器
 * 验证异常处理
 */
@ControllerAdvice //这个注解就是统一拦截异常处理
public class SellExceptionHandler {

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    //自定义异常处理  拦截登录异常  之前有抛出来的两个异常
    //http://sell.natapp4.cc/sell/wechat/qrAuthorize?returnUrl=http://sell.natapp4.cc/sell/seller/login
    @ExceptionHandler(value = SellerAuthorizeException.class)
    public ModelAndView SellerAuthorizeException(){
        //拼接url  重定向到二维码扫码登录界面
        return new ModelAndView("redirect:"
                .concat(projectUrlConfig.getWechatOpenAuthorize())
                .concat("/sell/wechat/qrAuthorize")
                .concat("?returnUrl=")
                .concat(projectUrlConfig.getSell())
                .concat("/sell/seller/login")
                );
    }

}
