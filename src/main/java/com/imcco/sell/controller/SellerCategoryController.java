package com.imcco.sell.controller;


import com.imcco.sell.dataobject.ProductCategory;
import com.imcco.sell.exception.SellException;
import com.imcco.sell.form.CategoryForm;
import com.imcco.sell.service.ProductCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 卖家
 */
@Controller
@RequestMapping("/seller/category")
public class SellerCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;


    /**
     * 商品类目列表
     * @param map
     * @return
     */
    @GetMapping("/list")
    public ModelAndView list(Map<String ,Object> map){
        List<ProductCategory> productCategoryList = productCategoryService.findAll();
        map.put("productCategoryList",productCategoryList);
        return  new ModelAndView("category/list");
    }

    /**
     * 展示
     * @param categoryId
     * @param map
     * @return
     */
    @GetMapping("/index")
    public ModelAndView index(@RequestParam(value = "categoryId",required = false) Integer categoryId,
                              Map<String,Object>map){
        if(categoryId!=null){
            ProductCategory productCategory = productCategoryService.findOne(categoryId);
            map.put("productCategory",productCategory);
        }
        return  new ModelAndView("category/index",map);
    }

    /**
     * 保存 更新
     * @param form
     * @param bindingResult
     * @param map
     * @return
     */
    @PostMapping("/save")
    public  ModelAndView save(@Valid CategoryForm form,
                              BindingResult bindingResult,
                                Map<String,Object>map){

        if(bindingResult.hasErrors()){
            //有错误时
            map.put("msg",bindingResult.getFieldError().getDefaultMessage());
            map.put("url","/sell/seller/category/index");
            return  new ModelAndView("common/error",map);
        }
        ProductCategory productCategory = new ProductCategory();
        try{
            if(form.getCategoryId()!=null){
                productCategory = productCategoryService.findOne(form.getCategoryId());
            }
            BeanUtils.copyProperties(form,productCategory);
            productCategoryService.save(productCategory);
        }catch (SellException e){
            map.put("msg", e.getMessage());
            map.put("url", "/sell/seller/category/index");
            return new ModelAndView("common/error", map);
        }
        map.put("url", "/sell/seller/category/list");
        return new ModelAndView("common/success", map);
    }

}
