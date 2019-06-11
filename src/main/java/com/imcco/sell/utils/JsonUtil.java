package com.imcco.sell.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 将对象格式化为json格式
 */
public class JsonUtil {

    public  static  String toJson(Object object){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(object);

    }
}
