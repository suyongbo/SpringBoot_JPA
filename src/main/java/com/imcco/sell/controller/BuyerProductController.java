package com.imcco.sell.controller;

import com.imcco.sell.VO.ProductInfoVO;
import com.imcco.sell.VO.ProductVO;
import com.imcco.sell.VO.ResultVO;
import com.imcco.sell.dataobject.ProductCategory;
import com.imcco.sell.dataobject.ProductInfo;
import com.imcco.sell.service.ProductCategoryService;
import com.imcco.sell.service.ProductInfoService;
import com.imcco.sell.utils.ResultVOUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 买家商品
 */
@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductInfoService productInfoService;//商品详情

    @Autowired
    private ProductCategoryService productCategoryService;//商品类目

    @GetMapping("/list")
    public ResultVO list(){
        //1.查询所有上架商品
        List<ProductInfo> productInfoList = productInfoService.findUpAll();

        //2.查询类目（一次性查询）
        //精简方法（java8，lambda）
        List<Integer> categoryTypeList = productInfoList.stream().map(e ->e.getCategoryType()).collect(Collectors.toList());
        List<ProductCategory> productCategoryList =productCategoryService.findByCategoryTypeIn(categoryTypeList);

        //3.数据拼装
        List<ProductVO> productVOList =  new ArrayList<>();
        for(ProductCategory productCategory :productCategoryList){//商品类目编号集合
            ProductVO productVO = new ProductVO();//商品（包含类目）
            productVO.setCategoryType(productCategory.getCategoryType());
            productVO.setCategoryName(productCategory.getCategoryName());

            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            for(ProductInfo productInfo :productInfoList){
                //判断每个商品的类型 是否属于这个这个类目
                if(productInfo.getCategoryType().equals(productCategory.getCategoryType())){
                    ProductInfoVO  productInfoVO = new ProductInfoVO();
                    //将商品信息表的数据 复制到数据传输对象中
                    BeanUtils.copyProperties(productInfo,productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            }
            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }
        return ResultVOUtil.success(productVOList);
    }

}
