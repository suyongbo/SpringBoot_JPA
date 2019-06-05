package com.imcco.sell.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 测试号配置文件
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat")//载入yml配置文件里前缀为wechat的配置信息
public class WechatAccountConfig {
        private  String mpAppId;
        private  String mpAppSecret;

}
