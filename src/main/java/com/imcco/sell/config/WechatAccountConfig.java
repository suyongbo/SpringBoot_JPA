package com.imcco.sell.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 测试号配置文件
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat")//载入yml配置文件里前缀为wechat的配置信息
public class WechatAccountConfig {

        //公众平台Id
        private  String mpAppId;

        //公众平台秘钥
        private  String mpAppSecret;

        //开放平台Id
        private  String openAppId;

        //开放平台秘钥
        private  String openAppSecret;

        //商户号
        private  String mchId;

        //商户秘钥
        private  String mchKey;

        //商户证书路径
        private  String keyPath;

        //微信支付异步通知地址
        private  String notifyUrl;

        //微信模板Id
        private Map<String,String> templateId;

}
