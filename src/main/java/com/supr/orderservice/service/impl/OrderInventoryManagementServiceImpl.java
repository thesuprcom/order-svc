package com.supr.orderservice.service.impl;

import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.entity.OrderItemEntity;
import com.supr.orderservice.enums.ItemStatusEnum;
import com.supr.orderservice.model.request.UpdateItemStatusRequest;
import com.supr.orderservice.model.request.UpdateQuantityRequest;
import com.supr.orderservice.service.OrderInventoryManagementService;
import com.supr.orderservice.service.external.InventoryServiceClient;
import com.supr.orderservice.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderInventoryManagementServiceImpl implements OrderInventoryManagementService {

  private final InventoryServiceClient inventoryServiceClient;


  @Override
  public void updateStoreOrderQuantity(OrderEntity order, UpdateQuantityRequest.OperationType operationType) {
    log.info("Updating Item quantity for order {} {}", order, operationType);
    order.getOrderItemEntities().forEach(orderItemVO -> {
      updateStoreItemQuantity(orderItemVO, operationType);
    });
  }

  @Override
  public void updateStoreOrderFutureQuantity(OrderEntity order, UpdateQuantityRequest.OperationType operationType) {
    log.info("Updating Item quantity for order {} {}", order, operationType);
    order.getOrderItemEntities().forEach(orderItemVO -> {
      updateStoreItemQuantity(orderItemVO, operationType, true);
    });
  }

  public void updateStoreItemQuantity(OrderItemEntity orderItemVO, UpdateQuantityRequest.OperationType operationType) {
    updateStoreItemQuantity(orderItemVO, operationType, false);
  }

  public void updateStoreItemQuantity(OrderItemEntity orderItemVO, UpdateQuantityRequest.OperationType operationType,
                                      boolean isSlottedQuantityUpdate) {
    UpdateQuantityRequest request = new UpdateQuantityRequest();
    request.setItemId(orderItemVO.getPskuCode());
    request.setOperationType(operationType);
    request.setOperationValue(orderItemVO.getOrderItemQuantity());
    request.setIsSlottedQuantityUpdate(isSlottedQuantityUpdate);
    try {
      inventoryServiceClient.updateQuantity(orderItemVO.getPskuCode(), request);
    } catch (Exception e) {
      log.error("Error updating item quantity with request {}", request, e);
    }
  }

  @Override
  public void updateStoreItemStatusForOrderItem(OrderItemEntity orderItemVO) {
    UpdateItemStatusRequest request = UpdateItemStatusRequest.builder()
        .itemId(orderItemVO.getPskuCode())
        .itemStatusEnum(getItemStatusEnum(orderItemVO))
        .quantity(orderItemVO.getOrderItemQuantity())
        .source(Constants.ORDER_SERVICE)
        .build();
    try {
      inventoryServiceClient.updateStoreItemStatus(orderItemVO.getSellerId(), request);
    } catch (Exception e) {
      log.error("Error updating item status with request {}", request, e);
    }
  }

  private ItemStatusEnum getItemStatusEnum(OrderItemEntity orderItemVO) {
    return orderItemVO.getCancellationReason().equalsIgnoreCase(Constants.WRONG_PRICE_REASON) ? ItemStatusEnum.DISABLE :
        ItemStatusEnum.OUT_OF_STOCK;
  }

  @Override
  public void updateStoreItemStatusForOrder(OrderEntity order) {
    log.info("Updating Item quantity for order {}", order);
    order.getOrderItemEntities().forEach(orderItemVO -> {
      updateStoreItemStatusForOrderItem(orderItemVO);
    });
  }

}
