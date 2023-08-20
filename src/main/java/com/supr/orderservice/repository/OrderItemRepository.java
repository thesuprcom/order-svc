package com.supr.orderservice.repository;

import com.supr.orderservice.entity.OrderItemEntity;
import com.supr.orderservice.enums.OrderItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {

  OrderItemEntity findFirstByOrderItemId(String orderItemId);

  List<OrderItemEntity> findAllByOrderIdIn(List<Long> orderIds);
  List<OrderItemEntity> findAllByStatusAndOrderIdIn(OrderItemStatus status, List<Long> orderIds);

  List<OrderItemEntity> findByOrderItemIdIn(List<String> orderItemIds);

  @Transactional
  @Modifying
  @Query(value = "delete from order_items where order_id = :orderId", nativeQuery = true)
  void deleteByOrderId(Long orderId);

}


