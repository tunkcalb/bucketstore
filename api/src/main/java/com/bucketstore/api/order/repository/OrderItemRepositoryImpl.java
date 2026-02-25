package com.bucketstore.api.order.repository;

import com.bucketstore.api.order.entity.OrderItem;
import com.bucketstore.api.order.entity.QOrderItem;
import com.bucketstore.api.product.entity.QProduct;
import com.bucketstore.api.product.entity.QProductItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OrderItemRepositoryImpl implements OrderItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<OrderItem> findItemsByOrderAndOptions(Long orderId, String productCode, String color, String size) {
        QOrderItem orderItem = QOrderItem.orderItem;
        QProductItem productItem = QProductItem.productItem;
        QProduct product = QProduct.product;

        return queryFactory
                .selectFrom(orderItem)
                .join(orderItem.productItem, productItem).fetchJoin() // 재고 정보 함께 조회
                .join(productItem.product, product).fetchJoin()      // 상품 코드 확인을 위해 조인
                .where(
                        orderItem.order.id.eq(orderId),
                        product.productCode.eq(productCode),
                        productItem.color.eq(color),
                        productItem.size.eq(size)
                )
                .fetch();
    }
}
