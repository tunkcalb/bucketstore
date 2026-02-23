package com.bucketstore.api.product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 상품코드
    @Column(nullable = false, unique = true)
    private String productCode;

    // 🔥 테스트 및 서비스에서 사용할 생성자 (id 제외)
    public Product(String productCode) {
        this.productCode = productCode;
    }
}
