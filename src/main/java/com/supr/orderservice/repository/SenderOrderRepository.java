package com.supr.orderservice.repository;

import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.enums.OrderItemStatus;
import com.supr.orderservice.enums.OrderType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SenderOrderRepository extends JpaRepository<OrderEntity, Long> {
    OrderEntity findByOrderIdAndOrderType(String orderId, OrderType orderType);
    OrderEntity findByCartIdentifierAndOrderType(String orderId, OrderType orderType);
    Page<OrderEntity> findByOrderType(OrderType orderType, Pageable pageable);

    OrderEntity findByOrderIdAndOrderTypeAndStatus(String orderId, OrderType orderType, OrderItemStatus status);
}
