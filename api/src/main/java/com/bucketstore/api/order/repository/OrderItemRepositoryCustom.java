package com.bucketstore.api.order.repository;

import com.bucketstore.api.order.entity.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepositoryCustom {

    List<OrderItem> findItemsByOrderAndOptions(Long orderId, String productCode, String color, String size);
}
