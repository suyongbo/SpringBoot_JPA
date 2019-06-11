package com.imcco.sell.controller;

import com.imcco.sell.dto.OrderDTO;
import com.imcco.sell.enums.ResultEnum;
import com.imcco.sell.exception.SellException;
import com.imcco.sell.service.OrderService;
import com.imcco.sell.service.PayService;
import com.lly835.bestpay.model.PayResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * 支付
 */
//这里不是返回Json格式的字符串了  是返回界面 所以用@Controller
@Controller
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PayService payService;

    @GetMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("returnUrl") String returnUrl,
                               Map<String,Object> map){
        //1.查询订单
        OrderDTO orderDTO = orderService.findOne(orderId);
        if(orderDTO==null){
            throw  new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        //2.发起支付
        PayResponse payResponse = payService.create(orderDTO);
        map.put("payResponse",payResponse);
        map.put("returnUrl",returnUrl);
        //将支付所需要的信息放入到模板里面
        return new ModelAndView("pay/create",map);
    }
}
