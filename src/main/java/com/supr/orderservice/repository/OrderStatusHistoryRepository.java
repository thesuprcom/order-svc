package com.supr.orderservice.repository;

import com.supr.orderservice.entity.OrderStatusHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusHistoryRepository
        extends JpaRepository<OrderStatusHistoryEntity, Long> {
}
