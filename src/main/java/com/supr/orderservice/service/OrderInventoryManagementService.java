package com.supr.orderservice.service;

import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.entity.OrderItemEntity;
import com.supr.orderservice.model.request.UpdateQuantityRequest;

public interface OrderInventoryManagementService {

  void updateStoreOrderFutureQuantity(OrderEntity order, UpdateQuantityRequest.OperationType operationType);

  void updateStoreOrderQuantity(OrderEntity order, UpdateQuantityRequest.OperationType operationType);

  void updateStoreItemQuantity(OrderItemEntity orderItemVO, UpdateQuantityRequest.OperationType operationType);

  void updateStoreItemStatusForOrder(OrderEntity order);

  void updateStoreItemStatusForOrderItem(OrderItemEntity orderItemVO);

}
