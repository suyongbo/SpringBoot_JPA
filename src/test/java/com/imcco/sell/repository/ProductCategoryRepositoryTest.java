package com.imcco.sell.repository;

import com.imcco.sell.dataobject.ProductCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {

    @Autowired
    private  ProductCategoryRepository repository;

    @Test
    public  void findOneTest(){
        Optional<ProductCategory> productCategory = repository.findById(1);
        ProductCategory productCategory1 = productCategory.get();
        System.out.println(productCategory1);
    }
    @Test
    @Transactional
    public  void saveTest(){
        ProductCategory productCategory = new ProductCategory("男生最爱",2);
        ProductCategory result = repository.save(productCategory);
        System.out.println(result);
    }
}