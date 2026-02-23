package com.bucketstore.api.product.repository;

import com.bucketstore.api.product.entity.ProductItem;

import java.util.Optional;

public interface ProductItemRepositoryCustom {
    // 상품코드, 색상, 사이즈를 조건으로 조회
    Optional<ProductItem> findProductItem(String productCode, String color, String size);
}
