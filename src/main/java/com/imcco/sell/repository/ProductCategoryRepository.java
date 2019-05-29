package com.imcco.sell.repository;

import com.imcco.sell.dataobject.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Integer> {
    //这里是自己写的一个  一般的增删改查是已经默认有了
    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);
}
