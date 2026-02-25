package com.bucketstore.api.order.repository;


import com.bucketstore.api.order.entity.Order;

import java.util.Optional;

public interface OrderRepositoryCustom {
    Optional<Order> findOrderWithItems(Long orderId);
}
