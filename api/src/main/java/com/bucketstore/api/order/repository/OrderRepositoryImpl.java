package com.bucketstore.api.order.repository;

import com.bucketstore.api.order.entity.Order;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
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

    @Override
    public Page<Order> findAllByMemberId(Long memberId, Pageable pageable) {
        // 1. 데이터 조회 쿼리 (최신순 페이징)
        List<Order> content = queryFactory
                .selectFrom(order)
                .where(order.member.id.eq(memberId))
                .offset(pageable.getOffset())   // 시작 지점
                .limit(pageable.getPageSize()) // 몇 개나 가져올지
                .orderBy(order.id.desc())      // 최신순
                .fetch();

        // 2. 전체 개수 쿼리 (페이징을 위해 필요)
        JPAQuery<Long> countQuery = queryFactory
                .select(order.count())
                .from(order)
                .where(order.member.id.eq(memberId));

        // 3. Page 객체로 변환해서 반환
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
