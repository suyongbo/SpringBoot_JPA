package com.imcco.sell.service.impl;

import com.imcco.sell.dataobject.ProductInfo;
import com.imcco.sell.dto.CartDTO;
import com.imcco.sell.enums.ProductStatusEnum;
import com.imcco.sell.enums.ResultEnum;
import com.imcco.sell.exception.SellException;
import com.imcco.sell.repository.ProductInfoRepository;
import com.imcco.sell.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {

    @Autowired
    private ProductInfoRepository repository;

    @Override
    public ProductInfo findOne(String productId) {
        //版本问题 多次封装 需要get()
        Optional<ProductInfo> productInfo1 = repository.findById(productId);
        ProductInfo productInfo = productInfo1.isPresent()?productInfo1.get():null;
        if (productInfo == null){
            throw new RuntimeException("查询结果为空");
        }
         return productInfo;
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return repository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return repository.save(productInfo);
    }

    /**
     * 上架
     * @param productId
     * @return
     */
    @Override
    public ProductInfo onSale(String productId) {
        ProductInfo productInfo = repository.findById(productId).get();
        if(productInfo==null){
            throw  new SellException(ResultEnum.PRODUCT_NOT_EXIST);
        }
        //判断商品是什么状态（上架还是下架）
        if(productInfo.getProductStatusEnum()==ProductStatusEnum.UP){
            throw  new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
        }
        //改变状态
        productInfo.setProductStatus(ProductStatusEnum.UP.getCode());
        return  repository.save(productInfo);

    }

    /**
     * 下架
     * @param productId
     * @return
     */
    @Override
    public ProductInfo offSale(String productId) {
        ProductInfo productInfo = repository.findById(productId).get();
        if(productInfo==null){
            throw  new SellException(ResultEnum.PRODUCT_NOT_EXIST);
        }
        //判断商品是什么状态（上架还是下架）
        if(productInfo.getProductStatusEnum()==ProductStatusEnum.DOWN){
            throw  new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
        }
        //改变状态
        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());
        return  repository.save(productInfo);
    }

    /**
     * 增加库存
     * @param cartDTOList
     */
    @Override
    @Transactional
    public void increaseStock(List<CartDTO> cartDTOList) {
        for(CartDTO cartDTO :cartDTOList){
            Optional<ProductInfo> productInfo1 = repository.findById(cartDTO.getProductId());
            ProductInfo productInfo = productInfo1.get();
            if(productInfo==null){
                throw  new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            Integer result =productInfo.getProductStock()+cartDTO.getProductQuantity();
            productInfo.setProductStock(result);
            repository.save(productInfo);
        }
    }

    /**
     * 减少库存
     * @param cartDTOList
     */
    @Override
    @Transactional
    public void decreaseStock(List<CartDTO> cartDTOList) {
        for(CartDTO cartDTO :cartDTOList){
            Optional<ProductInfo> productInfo1 = repository.findById(cartDTO.getProductId());
            ProductInfo productInfo = productInfo1.get();
            if(productInfo==null){
                throw  new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            Integer result =productInfo.getProductStock()-cartDTO.getProductQuantity();
            if(result<0){
                throw  new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
            }
            productInfo.setProductStock(result);
            repository.save(productInfo);
        }
    }
}
