package com.imcco.sell.controller;

import com.imcco.sell.dataobject.ProductInfo;
import com.imcco.sell.exception.SellException;
import com.imcco.sell.service.ProductCategoryService;
import com.imcco.sell.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * 卖家端商品
 */
@Controller
@RequestMapping("seller/product")
public class SellerProductController {

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("list")
    public ModelAndView list(@RequestParam(value = "page",defaultValue = "1") Integer page,
                             @RequestParam(value = "size",defaultValue = "2") Integer size,
                             Map<String,Object>map){
        PageRequest request = new PageRequest(page-1,size);
        Page<ProductInfo> productInfoPage = productInfoService.findAll(request);
        map.put("productInfoPage",productInfoPage);
        map.put("currentPage",page);
        map.put("size",size);
        return  new ModelAndView("product/list",map);
    }

    /**
     * 商品上架
     * @param productId
     * @param map
     * @return
     */
    @RequestMapping("onSale")
    public  ModelAndView onSale(@RequestParam("productId") String productId,
                                Map<String,Object>map){
        try{
            productInfoService.onSale(productId);
        }catch (SellException e){
            map.put("msg",e.getMessage());
            map.put("url","http://127.0.0.1:8080/sell/seller/product/list");
            return  new ModelAndView("common/error",map);
        }
        map.put("url","http://127.0.0.1:8080/sell/seller/product/list");
        return  new ModelAndView("common/success",map);
    }

    /**
     * 商品下架
     * @param productId
     * @param map
     * @return
     */
    @RequestMapping("offSale")
    public  ModelAndView offSale(@RequestParam("productId") String productId,
                                Map<String,Object>map){
        try{
            productInfoService.offSale(productId);
        }catch (SellException e){
            map.put("msg",e.getMessage());
            map.put("url","http://127.0.0.1:8080/sell/seller/product/list");
            return  new ModelAndView("common/error",map);
        }
        map.put("url","http://127.0.0.1:8080/sell/seller/product/list");
        return  new ModelAndView("common/success",map);
    }

}
