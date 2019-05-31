package com.imcco.sell.utils;

import java.util.Random;

/**
 * 产生随机数 规则 时间+随机数
 */
public class KeyUtil {
    //这里加一个同步  防止多个用户同时下单 造成产生数一样
    public  static synchronized String genUniqueKey(){
        Random random =  new Random();
        Integer number = random.nextInt(900000)+10000;
        String num;
        if(number==null){
            num=null;
        }
        num = number.toString();

        return  System.currentTimeMillis()+ num;
    }



}
