package com.imcco.sell.config;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 * 公众平台配置类  注意 公众平台和公众号用的是同一个接口 这里名字我会区别一下
 */
public class WechatOpenConfig {

    @Autowired
    private  WechatAccountConfig wechatAccountConfig;

    /**
     * 配置WxOpenService所需要的信息
     * @return
     */
    @Bean
    public WxMpService wxOpenService(){
        WxMpService wxOpenService = new WxMpServiceImpl();
        wxOpenService.setWxMpConfigStorage(wxOpenConfigStorage());
        return  wxOpenService;
    }

    /**
     * 设置公众平台的openAppId openAppSecret
     * @return
     */
    @Bean
    public WxMpConfigStorage wxOpenConfigStorage(){
        WxMpInMemoryConfigStorage wxMpInMemoryConfigStorage =  new WxMpInMemoryConfigStorage();
        wxMpInMemoryConfigStorage.setAppId(wechatAccountConfig.getOpenAppId());
        wxMpInMemoryConfigStorage.setSecret(wechatAccountConfig.getOpenAppSecret());
        return  wxMpInMemoryConfigStorage;

    }
}
