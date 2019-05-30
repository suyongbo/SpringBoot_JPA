package com.imcco.sell.service;

import com.imcco.sell.dataobject.ProductInfo;
import com.imcco.sell.dto.CartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 商品详情
 * 备注 库存的增加和减少  商品的上架和下架 接口实现类还没有完成
 */
public interface ProductInfoService {
    /**
     * 根据商品ID 也就是主键查询一个商品  返回一个商品对象
     * @param productId
     * @return
     */
    ProductInfo findOne(String productId);


    /**
     * up 在枚举里面表示上架的商品   这里便是查询所有上架的商品
     * @return
     */
    List<ProductInfo> findUpAll();

    /**
     * 商品的分页查询
     * @param pageable
     * @return
     */
    Page<ProductInfo> findAll(Pageable pageable);

    /**
     * 商品的新增和修改
     * @param productInfo
     * @return
     */
    ProductInfo save(ProductInfo productInfo);

    /**
     *商品的上架
     * @param productId
     * @return
     */
    ProductInfo onSale(String productId);

    /**
     * 商品的下架
     * @param productId
     * @return
     */
    ProductInfo offSale(String productId);

    /**
     * 增加商品库存
     * @param cartDTOList
     */
    void increaseStock(List<CartDTO> cartDTOList);

    /**
     * 减少商品库存
     * @param cartDTOList
     */
    void decreaseStock(List<CartDTO> cartDTOList);
}
