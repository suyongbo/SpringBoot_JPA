package com.imcco.sell.aspect;

import com.imcco.sell.Constant.CookieConstant;
import com.imcco.sell.Constant.RedisConstant;
import com.imcco.sell.exception.SellerAuthorizeException;
import com.imcco.sell.utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 切面 做身份验证   在每一个请求前都进行token验证
 */
@Aspect
@Component
@Slf4j
public class SellerAuthorizeAspect {

    @Autowired
    private StringRedisTemplate redisTemplate;

    //切点  切面规则  所有卖家端的请求都验证 不包括登录和登出（比避免重复）
    @Pointcut("execution(public * com.imcco.sell.controller.Seller*.*(..))"+
    "&& !execution(public * com.imcco.sell.controller.SellerUserController.*(..))")
    public  void verify(){}

    @Before("verify()")
    public  void doVerify(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //查询cookie
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if(cookie == null){
            log.warn("【登录效验】cookie中查不到token");
            throw new SellerAuthorizeException();
        }
        //redis里面的查询
        String  tokenValue = redisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_PREFIX,cookie.getValue()));
        if(StringUtils.isEmpty(tokenValue)){
            log.warn("【登录校验】Redis中查不到token");
            throw new SellerAuthorizeException();
        }
    }
}
