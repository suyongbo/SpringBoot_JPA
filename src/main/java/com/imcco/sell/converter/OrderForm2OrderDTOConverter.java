package com.imcco.sell.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.imcco.sell.dataobject.OrderDetail;
import com.imcco.sell.dto.OrderDTO;
import com.imcco.sell.enums.ResultEnum;
import com.imcco.sell.exception.SellException;
import com.imcco.sell.form.OrderForm;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 将orderForm对象转为orderDTO
 */
@Slf4j
public class OrderForm2OrderDTOConverter {
    //由于两个属性名不一样所以copy不了
    public  static OrderDTO convert(OrderForm orderForm){
        Gson gson = new Gson();
        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerPhone(orderForm.getPhone());
        orderDTO.setBuyerAddress(orderForm.getAddress());
        orderDTO.setBuyerOpenid(orderForm.getOpenid());

        //因为orderForm中的购物车属性 是一个String
            //我要把这个Json 转为一个集合
        List<OrderDetail> orderDetailList =   new ArrayList<>();
        try{
            orderDetailList=  gson.fromJson(orderForm.getItems(),
                    new TypeToken<List<OrderDetail>>(){}.getType());
        }catch (Exception e){
            log.error("【对象转换】错误，string={}",orderForm.getItems());
            throw  new SellException(ResultEnum.PARAM_ERROR);
        }
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }
}
