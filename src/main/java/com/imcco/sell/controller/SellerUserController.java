package com.imcco.sell.controller;

import com.imcco.sell.Constant.CookieConstant;
import com.imcco.sell.Constant.RedisConstant;
import com.imcco.sell.config.ProjectUrlConfig;
import com.imcco.sell.dataobject.SellerInfo;
import com.imcco.sell.enums.ResultEnum;
import com.imcco.sell.service.SellerService;
import com.imcco.sell.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 卖家用户 登录 登出
 */
@Controller
@RequestMapping("/seller")
public class SellerUserController {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    @GetMapping("/login")
    public ModelAndView login(@RequestParam("openid") String openid,
                              HttpServletResponse response,
                              Map<String,Object>map){
        //1.openid去和数据库的数据匹配
        SellerInfo sellerInfo = sellerService.findSellerInfoByOpenid(openid);
        if(sellerInfo==null){
            map.put("msg", ResultEnum.LOGIN_FAIL.getMessage());
            map.put("url",projectUrlConfig.getSell()+"/sell/seller/order/list");
            return  new ModelAndView("common/error",map);
        }
        //2.设置token至redis
        String token = UUID.randomUUID().toString();//UUID随机产生token
        Integer expire = RedisConstant.EXPIRE;//设置过期时间
        //redisTemplate.opsForValue()  表示对redis里面的value操作
        //String.format(RedisConstant.TOKEN_PREFIX,token  表示以RedisConstant.TOKEN_PREFIX开头的token
        //openid  用户的openid
        //expire 自定义的时间  TimeUnit.SECONDS 表示到了时间就过期
        redisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX,token),openid,expire, TimeUnit.SECONDS);

        //3.设置token至cookie
        CookieUtil.set(response, CookieConstant.TOKEN,token,expire);
        //注意这里的重定向 不能直接返回视图  直接访问会导致没有数据渲染
        return  new ModelAndView("redirect:"+ projectUrlConfig.getSell()+ "/sell/seller/order/list");
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest request,
                               HttpServletResponse response,
                               Map<String,Object>map){
        //1.在cookie中查询
        Cookie cookie =  CookieUtil.get(request,CookieConstant.TOKEN);
        if(cookie != null){
            //2.清除redis中的token
            redisTemplate.opsForValue().getOperations().delete(String.format(RedisConstant.TOKEN_PREFIX,cookie.getValue()));

            //3.清除cookie中的token  这里就是重新设置一遍  全部设置为null  0
            CookieUtil.set(response,CookieConstant.TOKEN,null,0);
        }
        map.put("msg",ResultEnum.LOGOUT_SUCCESS.getMessage());
        map.put("url",projectUrlConfig.getSell()+ "/sell/seller/order/list");
        return  new ModelAndView("common/success",map);
    }
}
