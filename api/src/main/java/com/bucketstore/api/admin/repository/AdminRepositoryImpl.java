package com.bucketstore.api.admin.repository;

import com.bucketstore.api.admin.dto.ProductInventoryResponse;
import com.bucketstore.api.order.entity.OrderItemType;
import com.bucketstore.api.order.entity.QOrderItem;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.bucketstore.api.product.entity.QProductItem.productItem;

@RequiredArgsConstructor
public class AdminRepositoryImpl implements AdminRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProductInventoryResponse> getProductInventoryStatus() {
        QOrderItem sub = new QOrderItem("sub");
        QOrderItem sub2 = new QOrderItem("sub2");

        // 특정 주문(Order) 내에서 해당 상품의 순 수량 (ORDER 수량 - CANCEL 수량)
        NumberExpression<Integer> netCountPerOrder = new CaseBuilder()
                .when(sub2.type.eq(OrderItemType.ORDER)).then(sub2.count)
                .otherwise(sub2.count.multiply(-1)).sum();

        // 유효한(완전히 취소되지 않은) 주문들 중 가장 최근의 ORDER 일시
        JPQLQuery<LocalDateTime> lastActiveOrderDate = JPAExpressions.select(sub.createdAt.max())
                .from(sub)
                .where(sub.productItem.id.eq(productItem.id)
                        .and(sub.type.eq(OrderItemType.ORDER))
                        .and(JPAExpressions.select(netCountPerOrder)
                                .from(sub2)
                                .where(sub2.order.id.eq(sub.order.id)
                                        .and(sub2.productItem.id.eq(sub.productItem.id))
                                        .and(sub2.productItem.color.eq(sub.productItem.color))
                                        .and(sub2.productItem.size.eq(sub.productItem.size)))
                                .gt(0)));

        return queryFactory
                .select(Projections.constructor(ProductInventoryResponse.class,
                        productItem.product.productCode,
                        productItem.color,
                        productItem.size,
                        productItem.stock,
                        // 최신 유효 주문 일시
                        lastActiveOrderDate
                ))
                .from(productItem)
                .fetch();
    }
}
