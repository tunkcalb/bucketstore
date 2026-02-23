package com.bucketstore.api.product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_items", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"product_id", "color", "size"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String color;
    private String size;
    private int stock; // 이 옵션의 재고

    /**
     * 재고 차감 및 복원 로직
     */
    public void decreaseStock(int quantity) {
        if (this.stock < quantity) {
            throw new RuntimeException("재고가 부족합니다.");
        }
        this.stock -= quantity;
    }

    public void increaseStock(int quantity) {
        this.stock += quantity;
    }
}
