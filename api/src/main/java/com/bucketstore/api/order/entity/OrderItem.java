package com.bucketstore.api.order.entity;

import com.bucketstore.api.product.entity.ProductItem;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_item_id")
    private ProductItem productItem;

    private int count;

    @Enumerated(EnumType.STRING)
    private OrderItemType type; // ORDER, CANCEL

    private LocalDateTime createdAt;

    public static OrderItem createOrderItem(Order order, ProductItem productItem, int count, OrderItemType type) {
        OrderItem item = new OrderItem();
        item.order = order;
        item.productItem = productItem;
        item.count = count;
        item.type = type;
        item.createdAt = LocalDateTime.now();

        if (type == OrderItemType.ORDER) {
            productItem.decreaseStock(count);
        } else {
            productItem.increaseStock(count);
        }
        return item;
    }
}
