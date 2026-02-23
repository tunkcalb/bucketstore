package com.bucketstore.api.product.repository;

import com.bucketstore.api.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
