package com.bucketstore.api.product.service;

import com.bucketstore.api.product.dto.ProductItemResponse;
import com.bucketstore.api.product.entity.ProductItem;
import com.bucketstore.api.product.repository.ProductItemRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductItemRepository productItemRepository;

    /**
     * 특정 상품의 특정 옵션(색상, 사이즈) 재고 정보를 조회
     */
    public ProductItemResponse getProductItem(String productCode, String color, String size) {
        ProductItem item = productItemRepository.findProductItem(productCode, color, size)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품 옵션입니다."));

        return ProductItemResponse.toDto(item); // DTO 변환 후 반환
    }
}
