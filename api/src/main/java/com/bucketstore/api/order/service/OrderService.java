package com.bucketstore.api.order.service;

import com.bucketstore.api.auth.entity.Member;
import com.bucketstore.api.auth.repository.MemberRepository;
import com.bucketstore.api.order.dto.OrderCancelDto;
import com.bucketstore.api.order.dto.OrderItemDto;
import com.bucketstore.api.order.dto.OrderResponseDto;
import com.bucketstore.api.order.entity.Order;
import com.bucketstore.api.order.entity.OrderItem;
import com.bucketstore.api.order.entity.OrderItemType;
import com.bucketstore.api.order.repository.OrderItemRepository;
import com.bucketstore.api.order.repository.OrderRepository;
import com.bucketstore.api.product.entity.ProductItem;
import com.bucketstore.api.product.repository.ProductItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductItemRepository productItemRepository;
    private final MemberRepository memberRepository;

    /**
     * [주문 생성]
     * 핵심 로직: 하나라도 재고 부족 시 전체 롤백
     */
    @Transactional
    public Long createOrder(Long memberId, List<OrderItemDto> requests) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        Order order = Order.createOrder(member);
        orderRepository.save(order);

        // 상품 코드별로 정렬해서 데드락 방지
        List<OrderItemDto> sortedRequests = requests.stream()
                .sorted(Comparator.comparing(OrderItemDto::getProductCode))
                .toList();

        for (OrderItemDto req : sortedRequests) {
            // 1. 비관적 락으로 상품 조회
            ProductItem productItem = productItemRepository.findProductItemForUpdate(req.getProductCode(), req.getColor(), req.getSize())
                    .orElseThrow(() -> new IllegalArgumentException("상품 없음"));

            // 2. OrderItem 생성 시점에 재고 차감 로직 실행
            OrderItem orderItem = OrderItem.createOrderItem(order, productItem, req.getCount(), OrderItemType.ORDER);
            orderItemRepository.save(orderItem);
        }
        return order.getId();
    }

    /**
     * [부분 취소]
     * 특정 상품의 수량만 취소하고 행 추가
     */
    @Transactional
    public void cancelOrderItem(Long memberId, Long orderId, OrderCancelDto dto) {
        // 1. 주문 조회 및 소유권 확인
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        if (!order.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("주문에 대한 권한이 없습니다.");
        }

        // 2. 옵션에 맞는 상품에 락을 걸고 조회
        ProductItem productItem = productItemRepository
                .findProductItemForUpdate(dto.getProductCode(), dto.getColor(), dto.getSize())
                .orElseThrow(() -> new IllegalArgumentException("일치하는 상품 옵션이 없습니다."));

        // 3. 해당 상품의 PK(ID)를 추출해서 현재 이 주문에서 몇 개나 남았는지 계산
        int currentQuantity = calculateCurrentQty(orderId, productItem.getProduct().getProductCode(), productItem.getColor(), productItem.getSize());

        if (currentQuantity < dto.getCancelCount()) {
            throw new IllegalArgumentException("취소 가능 수량을 초과했습니다. 현재 남은 수량: " + currentQuantity);
        }

        // 4. CANCEL 행 추가
        OrderItem cancelItem = OrderItem.createOrderItem(
                order,
                productItem,
                dto.getCancelCount(),
                OrderItemType.CANCEL
        );

        orderItemRepository.save(cancelItem);
    }

    private int calculateCurrentQty(Long orderId, String productCode, String color, String size) {
        // 1. 해당 주문 내에서 '특정 상품 옵션'에 해당하는 모든 OrderItem 기록을 가져옴
        List<OrderItem> items = orderItemRepository.findItemsByOrderAndOptions(orderId, productCode, color, size);

        // 2. ORDER는 더하고, CANCEL은 빼서 최종 잔여 수량 계산
        return items.stream()
                .mapToInt(item -> item.getType() == OrderItemType.ORDER ? item.getCount() : -item.getCount())
                .sum();
    }

    /**
     * 주문 내역 조회
     */
    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getMyOrders(Long memberId, int page, int size) {
        // page: 페이지 번호, size: 한 페이지에 보여줄 개수
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Order> orderPage = orderRepository.findAllByMemberId(memberId, pageRequest);

        return orderPage.map(this::convertToDto);
    }

    private OrderResponseDto convertToDto(Order order) {
        List<OrderResponseDto.OrderItemDetailDto> itemDetails = order.getOrderItems().stream()
                // 1. 상품 옵션별로 그룹화
                .collect(Collectors.groupingBy(
                        item -> item.getProductItem().getProduct().getProductCode() + "-" + item.getProductItem().getColor() + "-" + item.getProductItem().getSize()
                ))
                .values().stream()
                .map(items -> {
                    OrderItem item = items.get(0);

                    // 2. 상세 이력 리스트 생성 (HistoryDto 변환)
                    List<OrderResponseDto.OrderItemDetailDto.HistoryDto> histories = items.stream()
                            .map(i -> new OrderResponseDto.OrderItemDetailDto.HistoryDto(
                                    i.getType().name(), i.getCount(), i.getCreatedAt()))
                            .toList();

                    // 3. 최종 수량 합산 (ORDER는 +, CANCEL은 -)
                    int totalCount = items.stream()
                            .mapToInt(i -> i.getType() == OrderItemType.ORDER ? i.getCount() : -i.getCount())
                            .sum();

                    return new OrderResponseDto.OrderItemDetailDto(
                            item.getProductItem().getProduct().getProductCode(), item.getProductItem().getColor(), item.getProductItem().getSize(),
                            totalCount, histories
                    );
                })
                .toList();

        return new OrderResponseDto(order.getId(), order.getOrderDate(), itemDetails);
    }
}
