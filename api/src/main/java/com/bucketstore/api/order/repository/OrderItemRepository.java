package com.bucketstore.api.order.repository;

import com.bucketstore.api.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> , OrderItemRepositoryCustom {
}
