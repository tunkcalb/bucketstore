package com.bucketstore.api.product.repository;

import com.bucketstore.api.product.entity.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductItemRepository extends JpaRepository<ProductItem, Long>, ProductItemRepositoryCustom {
}
