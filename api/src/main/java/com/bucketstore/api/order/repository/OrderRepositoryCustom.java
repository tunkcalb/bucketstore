package com.bucketstore.api.order.repository;


import com.bucketstore.api.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderRepositoryCustom {
    Optional<Order> findOrderWithItems(Long orderId);

    Page<Order> findAllByMemberId(Long memberId, Pageable pageable);
}
