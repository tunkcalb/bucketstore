package com.bucketstore.api.admin.repository;

import com.bucketstore.api.admin.dto.ProductInventoryResponse;
import java.util.List;

public interface AdminRepositoryCustom {
    // 모든 상품의 재고와 마지막 유효 주문 일시 조회
    List<ProductInventoryResponse> getProductInventoryStatus();
}
