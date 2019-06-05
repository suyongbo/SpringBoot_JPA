package com.imcco.sell.controller;

import com.imcco.sell.enums.ResultEnum;
import com.imcco.sell.exception.SellException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.security.sasl.SaslException;
import java.net.URLEncoder;

/**
 * 该类的接口用于对接微信网页授权 授权完成跳转到指定网页
 */
@Slf4j
@Controller
@RequestMapping("/wechat")
public class WechatController {

    @Autowired
    private WxMpService wxMpService;

    /**
     * 获得code
     * @return
     */
    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl){
        //暂时将我们的回调地址编码放在这里
        String url = "http://godsu.mynatapp.cc/wechat/userInfo";
        //获取微信返回的重定向url
        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_USERINFO, URLEncoder.encode(returnUrl));
        log.info("【微信网页授权】获取code,redirectUrl={}",redirectUrl);
        return "redirect:"+redirectUrl;
    }

    /**
     * 用code换取access_token信息
     * @param code
     * @param returnUrl
     * @return
     */
    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code,@RequestParam("state") String returnUrl){
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken;
        try{
            //用code换取access_token信息
            wxMpOAuth2AccessToken =wxMpService.oauth2getAccessToken(code);
        }catch (WxErrorException e){
            log.error("【微信网页授权】异常，{}",e);
            throw  new SellException(ResultEnum.WECHAT_MP_ERROR.getCode(),e.getError().getErrorMsg());
        }
        //从access_token中获取用户的openid
        String openId = wxMpOAuth2AccessToken.getOpenId();
        log.info("【微信网页授权】获取openId,openId={}",openId);
            return "redirect:"+returnUrl+"?openid="+openId;
    }


}
