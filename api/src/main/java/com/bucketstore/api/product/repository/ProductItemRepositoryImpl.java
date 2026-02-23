package com.bucketstore.api.product.repository;

import com.bucketstore.api.product.entity.ProductItem;
import com.bucketstore.api.product.entity.QProduct;
import com.bucketstore.api.product.entity.QProductItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
}
