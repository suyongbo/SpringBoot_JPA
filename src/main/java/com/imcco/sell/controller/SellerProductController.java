package com.imcco.sell.controller;

import com.imcco.sell.dataobject.ProductCategory;
import com.imcco.sell.dataobject.ProductInfo;
import com.imcco.sell.exception.SellException;
import com.imcco.sell.form.ProductForm;
import com.imcco.sell.service.ProductCategoryService;
import com.imcco.sell.service.ProductInfoService;
import com.imcco.sell.utils.KeyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
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

    @GetMapping("/index")// value = "productId",required = false  这里表示这个字段不是必须要传的
    public  ModelAndView index(@RequestParam( value = "productId",required = false) String productId,
                               Map<String,Object>map){
        //先判断传了没有
        if(!StringUtils.isEmpty(productId)){//不为空的时候
            ProductInfo productInfo = productInfoService.findOne(productId);
            map.put("productInfo",productInfo);
        }
        //查询商品的所有类目
        List<ProductCategory> productCategoryList = productCategoryService.findAll();
        map.put("productCategoryList",productCategoryList);

        return  new ModelAndView("product/index",map);
    }

    /**
     * 商品的更新和新增
     * @param form
     * @param bindingResult
     * @param map
     * @return
     */
    @PostMapping("/save")
    public  ModelAndView  save(@Valid ProductForm form,
                               BindingResult bindingResult,
                               Map<String ,Object>map){
        //先判断form是否有错误  这里的form我没有做之前的验证
        if(bindingResult.hasErrors()){//如果有错误
            map.put("msg",bindingResult.getFieldError().getDefaultMessage());
            map.put("url","http://127.0.0.1:8080/sell/seller/product/index");
            return  new ModelAndView("common/error",map);
        }
        ProductInfo productInfo = new ProductInfo();
        try{
            //先判断productId是否为空  为空则表示是新增
            if(!StringUtils.isEmpty(form.getProductId())){
                //不为空时
                productInfo = productInfoService.findOne(form.getProductId());
            }else {
                //为空时 自己随机一个productId  还需要给他设置一个productType
                form.setProductId(KeyUtil.genUniqueKey());
                productInfo.setProductType(form.getCategoryType());
            }
            BeanUtils.copyProperties(form,productInfo);
            productInfoService.save(productInfo);
        }catch(SellException e){
            map.put("msg",e.getMessage());
            map.put("url","http://127.0.0.1:8080/sell/seller/product/index");
            return new ModelAndView("common/error",map);
        }

        map.put("url","http://127.0.0.1:8080/sell/seller/product/list");
        return  new ModelAndView("common/success",map);
    }
}
