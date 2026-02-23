package com.bucketstore.api.product.repository;

import com.bucketstore.api.product.entity.ProductItem;
import com.bucketstore.api.product.entity.QProduct;
import com.bucketstore.api.product.entity.QProductItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class ProductItemRepositoryImpl implements ProductItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<ProductItem> findProductItem(String productCode, String color, String size) {

        QProduct product = QProduct.product;
        QProductItem productItem = QProductItem.productItem;

        return Optional.ofNullable(
                queryFactory
                        .selectFrom(productItem)
                        .join(productItem.product, product).fetchJoin() // 연관된 Product 한 번에 가져오기
                        .where(
                                product.productCode.eq(productCode),
                                productItem.color.eq(color),
                                productItem.size.eq(size)
                        )
                        .fetchOne()
        );
    }

    @Override
    public Optional<ProductItem> findProductItemForUpdate(String productCode, String color, String size) {
        QProduct product = QProduct.product;
        QProductItem productItem = QProductItem.productItem;

        return Optional.ofNullable(
                queryFactory
                        .selectFrom(productItem)
                        .join(productItem.product, product).fetchJoin()
                        .where(
                                product.productCode.eq(productCode),
                                productItem.color.eq(color),
                                productItem.size.eq(size)
                        )
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE) // 비관적 락 적용: 동시 주문 시 재고 데이터 불일치 방지 (SELECT ... FOR UPDATE)
                        .fetchOne()
        );
    }
}
