package com.imcco.sell.service.impl;

import com.imcco.sell.dataobject.ProductCategory;
import com.imcco.sell.repository.ProductCategoryRepository;
import com.imcco.sell.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryRepository repository;

    @Override
    public ProductCategory findOne(Integer categoryId) {
        //这里涉及版本文艺  有一个多次封装的问题  所以需要再get()一下
        Optional<ProductCategory> productCategory= repository.findById(categoryId);
        return productCategory.get();
    }

    @Override
    public List<ProductCategory> findAll() {
        return repository.findAll();
    }

    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> CategoryTypeList) {
        return repository.findByCategoryTypeIn(CategoryTypeList);
    }

    @Override
    public ProductCategory save(ProductCategory productCategory) {
        return repository.save(productCategory);
    }
}
