package com.bucketstore.api.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class OrderResponseDto {
    private Long orderId;
    private LocalDateTime orderDate;
    private List<OrderItemDetailDto> orderItems; // 상품별 상세 정보

    @Getter
    @AllArgsConstructor
    public static class OrderItemDetailDto {
        private String productCode;
        private String color;
        private String size;
        private int totalCount;        // [합산] 최종 남은 수량
        private List<HistoryDto> histories; // [상세] 사고 취소한 기록들

        @Getter
        @AllArgsConstructor
        public static class HistoryDto {
            private String type;       // ORDER 또는 CANCEL
            private int count;         // 당시 수량
            private LocalDateTime createdAt; // 기록 시점
        }
    }
}
