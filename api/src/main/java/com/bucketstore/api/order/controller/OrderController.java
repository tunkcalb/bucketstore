package com.bucketstore.api.order.controller;

import com.bucketstore.api.order.dto.CreateOrderDto;
import com.bucketstore.api.order.dto.OrderCancelDto;
import com.bucketstore.api.order.dto.OrderResponseDto;
import com.bucketstore.api.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/order")
@RestController
public class OrderController {

    private final OrderService orderService;

    /**
     * 주문 생성 API
     * @param createOrderDto 주문 정보
     * @return 생성된 주문 ID
     */
    @PostMapping
    public ResponseEntity<Long> createOrder(
            // TODO: JWT 인증 적용 후 @AuthenticationPrincipal 어노테이션으로 사용자 정보 받아오기
            /* @AuthenticationPrincipal UserPrincipal userPrincipal, */
            @Valid @RequestBody CreateOrderDto createOrderDto
    ) {
        // Long memberId = userPrincipal.getId();
        Long memberId = 1L; // 임시로 사용자 ID를 1로 고정
        var orderId = orderService.createOrder(memberId, createOrderDto.getItems());
        return ResponseEntity.ok(orderId);
    }

    /**
     * 주문 부분 취소 API
     * @param orderId 주문 ID
     * @param orderCancelDto 취소할 상품 정보
     * @return 성공 여부
     */
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrderItem(
            // TODO: JWT 인증 적용 후 @AuthenticationPrincipal 어노테이션으로 사용자 정보 받아오기
            /* @AuthenticationPrincipal UserPrincipal userPrincipal, */
            @PathVariable Long orderId,
            @Valid @RequestBody OrderCancelDto orderCancelDto
    ) {
        // Long memberId = userPrincipal.getId();
        Long memberId = 1L; // 임시로 사용자 ID를 1로 고정
        orderService.cancelOrderItem(memberId, orderId, orderCancelDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponseDto>> getMyOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        // Long memberId = userPrincipal.getId();
        Long memberId = 1L; // 임시로 사용자 ID를 1로 고정

        Page<OrderResponseDto> responses = orderService.getMyOrders(memberId, page, size);
        return ResponseEntity.ok(responses);
    }
}
