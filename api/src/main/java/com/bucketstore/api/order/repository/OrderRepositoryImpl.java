package com.bucketstore.api.order.repository;

import com.bucketstore.api.order.entity.Order;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.bucketstore.api.order.entity.QOrder.order;
import static com.bucketstore.api.order.entity.QOrderItem.orderItem;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Order> findOrderWithItems(Long orderId) {
        Order result = queryFactory
                .selectFrom(order)
                .leftJoin(order.orderItems, orderItem).fetchJoin() // 아이템들까지
                .where(order.id.eq(orderId))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
