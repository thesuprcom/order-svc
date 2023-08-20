package com.supr.orderservice.service;

import com.supr.orderservice.entity.OrderItemEntity;
import com.supr.orderservice.enums.OrderItemEvent;
import com.supr.orderservice.model.ItemInfo;
import com.supr.orderservice.model.OrderPrice;
import com.supr.orderservice.model.request.OrderItemReplacementRequest;

import java.math.BigDecimal;
import java.util.List;

public interface OrderItemService {

  OrderItemEntity save(OrderItemEntity orderItemVO);

  List<OrderItemEntity> findAllByOrderIdIn(List<Long> orderIds);

  List<OrderItemEntity> saveAll(List<OrderItemEntity> orderItems);

  OrderItemEntity fetchByOrderItemId(String orderItemId);

  void replaceOrderItem(OrderItemEntity orderItemToReplace, List<OrderItemReplacementRequest> replacementOrderItems);

  List<OrderItemEntity> fetchByOrderItemIdIn(List<String> orderItemIds);

  void changeOrderItemStatus(OrderItemEntity orderItemVO, OrderItemEvent oie, String reason);

  OrderPrice recalculatePriceForAcceptedItem(OrderPrice originalPrice, ItemInfo item, BigDecimal itemPrice);

  OrderItemEntity getReturnOrderItem(OrderItemEntity orderItemVO, BigDecimal quantity);

  void deleteAllOrderItems(Long orderId);

}
