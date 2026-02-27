package com.bucketstore.api.admin.repository;

import com.bucketstore.api.product.entity.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<ProductItem, Long>, AdminRepositoryCustom {
}
