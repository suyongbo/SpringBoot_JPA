package com.imcco.sell.service;

import com.imcco.sell.dataobject.ProductCategory;

import java.util.List;

/**
 * 商品类目
 */

public interface ProductCategoryService {
    /**
     *查找一个 根据主键查询
     * @param categoryId  类目主键Id
     * @return 返回一个ProductCategory (类目)对象
     */
    ProductCategory findOne(Integer categoryId);

    /**
     * 查询所有  不带参数
     * @return 返回一个 ProductCategory (类目)对象 的集合
     */
    List<ProductCategory> findAll();

    /**
     * 根据商品编号的集合查询对应的所有商品
     * @param CategoryTypeList 商品类目编号集合
     * @return  返回一个 ProductCategory (类目)对象 的集合
     */
    List<ProductCategory> findByCategoryTypeIn(List<Integer> CategoryTypeList);

    /**
     * 新增 或者修改类目对象
     * @param productCategory
     * @return 返回一个类目对象
     */
    ProductCategory save(ProductCategory productCategory);

}
