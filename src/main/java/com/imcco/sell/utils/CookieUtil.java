package com.imcco.sell.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Cookie工具类
 */
public class CookieUtil {
    /**
     * 设置cookie
     * @param response
     * @param name
     * @param value
     * @param maxAge
     */
    public  static  void set(HttpServletResponse response,
                             String name,
                             String value,
                             int maxAge){
        Cookie cookie = new Cookie(name,value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    /**
     * 获取cookie
     * @param request
     * @param name
     * @return
     */
    public static Cookie get(HttpServletRequest request,
                             String name){
        Map<String,Cookie> cookieMap = readCookieMap(request);
        //判断Map集合对象中是否包含指定的键名
        if(cookieMap.containsKey(name)){
            return  cookieMap.get(name);
        }else {
            return  null;
        }
    }

    /**
     * 讲cookie 封装成Map
     * @param request
     * @return
     */
    private  static  Map<String,Cookie> readCookieMap(HttpServletRequest request){

        Map<String,Cookie> cookieMap = new HashMap<>();
        //从转发请求中获取cookie  注意返回的是数组
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for(Cookie cookie :cookies){
                cookieMap.put(cookie.getName(),cookie);
            }
        }
        return cookieMap;
    }
}
