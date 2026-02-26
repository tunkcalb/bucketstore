package com.bucketstore.api.order.service;

import com.bucketstore.api.auth.entity.Member;
import com.bucketstore.api.auth.repository.MemberRepository;
import com.bucketstore.api.order.dto.CreateOrderDto;
import com.bucketstore.api.order.dto.OrderCancelDto;
import com.bucketstore.api.order.dto.OrderItemDto;
import com.bucketstore.api.order.dto.OrderResponseDto;
import com.bucketstore.api.order.entity.OrderItem;
import com.bucketstore.api.order.entity.OrderItemType;
import com.bucketstore.api.order.repository.OrderItemRepository;
import com.bucketstore.api.order.repository.OrderRepository;
import com.bucketstore.api.product.entity.Product;
import com.bucketstore.api.product.entity.ProductItem;
import com.bucketstore.api.product.repository.ProductItemRepository;
import com.bucketstore.api.product.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductItemRepository productItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void cleanUp() {
        // 가장 말단 자식들부터 삭제
        orderItemRepository.deleteAllInBatch();

        orderRepository.deleteAllInBatch();
        productItemRepository.deleteAllInBatch();

        memberRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("동시에 100명이 주문할 때, 재고만큼만 주문이 성공해야 한다")
    void concurrencyOrderTest() throws InterruptedException {

        String productCode = "11101JS505";
        String color = "WH";
        String size = "95";

        // 1. [데이터 준비] Member, Product, ProductItem을 저장
        Member member = memberRepository.save(new Member("user_1"));
        Long memberId = member.getId();

        Product product = productRepository.save(new Product(productCode));

        // 초기 재고 10개 설정
        ProductItem savedItem = productItemRepository.save(
                new ProductItem(product, color, size, 10)
        );

        // 2. [테스트 설정]
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        OrderItemDto orderItemDto = new OrderItemDto(productCode, color, size, 1);
        CreateOrderDto request = new CreateOrderDto(Arrays.asList(orderItemDto));

        // 3. [실행]
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    orderService.createOrder(memberId, request.getItems());
                } catch (Exception e) {
                    // 재고가 다 떨어지면 "재고 부족" 예외 발생
                    System.out.println("주문 실패: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // 4. [검증]
        ProductItem productItem = productItemRepository
                .findProductItem(productCode, color, size)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        // 10개 재고에 100명이 주문했으니 남은 재고는 0이어야 함
        assertThat(productItem.getStock()).isEqualTo(0);
    }

    @Test
    @DisplayName("주문을 취소하면 취소 레코드가 추가되고 재고가 정상 복구된다")
    void cancelOrderTest() {

        String productCode = "11101JS505";
        String color = "WH";
        String size = "95";
        // 1. 주문 정보
        Member member = memberRepository.save(new Member("user_1"));
        Product product = productRepository.save(new Product(productCode));
        ProductItem item = productItemRepository.save(new ProductItem(product, color, size, 10));


        // 1개 주문 실행 (재고 10 -> 8)
        OrderItemDto dto = new OrderItemDto(productCode, color, size, 2);
        Long orderId = orderService.createOrder(member.getId(), List.of(dto));

        // 2. 하나만 주문 취소 실행!
        OrderCancelDto cancelDto = new OrderCancelDto(productCode, color, size, 1);
        orderService.cancelOrderItem(member.getId(), orderId, cancelDto);

        // 3. 검증
        // A. 재고가 9개인지 확인
        ProductItem afterCancel = productItemRepository.findById(item.getId()).orElseThrow();
        assertThat(afterCancel.getStock()).isEqualTo(9);

        // B. OrderItem 테이블에 이 주문(orderId)으로 된 데이터가 2개인지 확인
        // (기존 ORDER 2개 + 방금 넣은 CANCEL 1개)
        List<OrderItem> orderItems = orderItemRepository.findItemsByOrderAndOptions(orderId, productCode, color, size);
        assertThat(orderItems).hasSize(2);

        // C. 추가된 아이템의 상태가 CANCEL인지 확인
        boolean hasCancelRecord = orderItems.stream()
                .anyMatch(orderItem -> orderItem.getType() == OrderItemType.CANCEL);
        assertThat(hasCancelRecord).isTrue();
    }

    @Test
    @DisplayName("내 주문 목록 조회 시 합산 수량과 이력이 정확히 계산되어야 한다")
    void getMyOrders_Success() {

        // 주문 정보
        String productCode = "11101JS505";
        String color = "WH";
        String size = "95";
        Member member = memberRepository.save(new Member("user_1"));
        Product product = productRepository.save(new Product(productCode));
        ProductItem item = productItemRepository.save(new ProductItem(product, color, size, 10));

        // 1번 주문 (3개 구매 후 1개 취소)
        List<OrderItemDto> items = List.of(new OrderItemDto(productCode, color, size, 3));
        Long orderId = orderService.createOrder(member.getId(), items);

        OrderCancelDto cancelDto = new OrderCancelDto(productCode, color, size, 1);
        orderService.cancelOrderItem(member.getId(), orderId, cancelDto);

        // 조회 실행 (0페이지, 5개씩)
        Page<OrderResponseDto> result = orderService.getMyOrders(member.getId(), 0, 5);

        // 검증
        assertThat(result.getContent()).hasSize(1); // 주문은 1건
        OrderResponseDto response = result.getContent().get(0);

        // 상품 상세 정보 검증
        assertThat(response.getOrderItems()).hasSize(1);
        OrderResponseDto.OrderItemDetailDto detail = response.getOrderItems().get(0);

        assertThat(detail.getTotalCount()).isEqualTo(2); // 3 - 1 = 2개여야 함
        assertThat(detail.getHistories()).hasSize(2);    // ORDER, CANCEL 총 2건의 이력
        assertThat(detail.getHistories().get(0).getType()).isEqualTo("ORDER");
        assertThat(detail.getHistories().get(1).getType()).isEqualTo("CANCEL");
    }

    @Test
    @DisplayName("페이징 처리가 정상적으로 동작해야 한다")
    void getMyOrders_Paging() {

        // 주문 정보
        String productCode = "11101JS505";
        String color = "WH";
        String size = "95";
        Member member = memberRepository.save(new Member("user_1"));
        Product product = productRepository.save(new Product(productCode));
        ProductItem item = productItemRepository.save(new ProductItem(product, color, size, 10));

        // 주문 6건 생성
        for (int i = 0; i < 6; i++) {
            List<OrderItemDto> items = List.of(new OrderItemDto(productCode, color, size, 1));
            orderService.createOrder(member.getId(), items);
        }

        // 0페이지 조회 (사이즈 5)
        Page<OrderResponseDto> page0 = orderService.getMyOrders(member.getId(), 0, 5);
        // 1페이지 조회 (사이즈 5)
        Page<OrderResponseDto> page1 = orderService.getMyOrders(member.getId(), 1, 5);

        // 검증
        assertThat(page0.getContent()).hasSize(5);   // 첫 페이지 5개
        assertThat(page1.getContent()).hasSize(1);   // 두 번째 페이지 1개
        assertThat(page0.getTotalElements()).isEqualTo(6); // 전체는 6개
        assertThat(page0.getTotalPages()).isEqualTo(2);    // 총 2페이지
    }
}