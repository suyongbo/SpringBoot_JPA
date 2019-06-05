package com.imcco.sell.config;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 微信网页授权信息配置类
 */
@Component//把普通pojo实例化到spring容器中，相当于配置文件中的<bean id="" class=""/>
public class WeChatMPConfig {

    @Autowired
    private  WechatAccountConfig wechatAccountConfig;

    /**
     * 配置WxMpService所需要的信息
     * @return
     */
    @Bean// 此注解指定在Spring容器启动时，就执行该方法并将该方法返回的对象交由Spring容器管理
    public WxMpService wxMpService(){
        WxMpService wxMpService = new WxMpServiceImpl();
        //设置配置信息的存储位置
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage());
        return  wxMpService;
    }

    /**
     * 配置appID和appsecret
     * @return
     */
    @Bean
    public WxMpConfigStorage wxMpConfigStorage(){
        //使用这个实现类表示将配置信息存储在内存中
        WxMpInMemoryConfigStorage wxMpInMemoryConfigStorage = new WxMpInMemoryConfigStorage();
        wxMpInMemoryConfigStorage.setAppId(wechatAccountConfig.getMpAppId());
        wxMpInMemoryConfigStorage.setSecret(wechatAccountConfig.getMpAppSecret());
        return wxMpInMemoryConfigStorage;
    }



}
