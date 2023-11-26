package com.supr.orderservice.repository;

import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.enums.OrderItemStatus;
import com.supr.orderservice.enums.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiverOrderRepository extends JpaRepository<OrderEntity, Long> {
    OrderEntity findByOrderIdAndOrderType(String orderId, OrderType orderType);

    OrderEntity findByOrderIdAndOrderTypeAndStatus(String orderId, OrderType orderType, OrderItemStatus status);
}
