package com.supr.orderservice.repository;

import com.supr.orderservice.entity.OrderItemEntity;
import com.supr.orderservice.entity.OrderItemStatusHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemStatusHistoryRepository extends JpaRepository<OrderItemStatusHistoryEntity, OrderItemEntity> {

  List<OrderItemStatusHistoryEntity> findOrderItemStatusHistoryEntityByOrderItem(OrderItemEntity orderItem);

}
