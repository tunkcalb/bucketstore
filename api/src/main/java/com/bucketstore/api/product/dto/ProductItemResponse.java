package com.bucketstore.api.product.dto;

import com.bucketstore.api.product.entity.ProductItem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductItemResponse {

    // 1. 상품 정보 (Product 엔티티에서 가져옴)
    private String productCode;

    // 2. 상세 옵션 정보 (ProductItem 엔티티에서 가져옴)
    private String color;
    private String size;
    private int stock;

    /**
     * Entity -> DTO 변환
     */
    public static ProductItemResponse toDto(ProductItem item) {
        return new ProductItemResponse(
                item.getProduct().getProductCode(),
                item.getColor(),
                item.getSize(),
                item.getStock()
        );
    }
}
