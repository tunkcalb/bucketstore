package com.bucketstore.api.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ProductInventoryResponse {
    private String productCode;
    private String color;
    private String size;
    private Integer currentStock;       // 현재 재고량
    private LocalDateTime lastValidOrderDate; // 마지막 유효 주문 일시 (취소 제외 최신)
}
