package com.supr.orderservice.service;

import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.enums.DashboardType;
import com.supr.orderservice.enums.ExternalStatus;
import com.supr.orderservice.enums.OrderItemEvent;
import com.supr.orderservice.enums.OrderItemStatus;
import com.supr.orderservice.model.OrderItem;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderService {

  List<OrderEntity> findByOrderId(String orderId);

  OrderEntity fetchSenderOrder(String orderId);
  OrderEntity fetchReceiverOrder(String orderId);
  OrderEntity fetchReceiverOrderWithStatus(String orderId);
  OrderEntity fetchSenderOrderWithStatus(String orderId);

  OrderEntity fetchOrderForStore(String orderId, String storeId);

  Page<OrderEntity> fetchOrdersByUserId(String id, String countryCode, Pageable pageable);

  OrderEntity save(OrderEntity order);

  OrderEntity cancelOrderEntity(OrderEntity order, String message, OrderItemEvent orderItemEvent,
                                boolean shouldUpdateInventoryQty);

  Page<OrderEntity> findByStoreId(String storeId, Pageable pageable);

  Page<OrderEntity> findByExternalStatusIn(List<ExternalStatus> externalStatuses, Pageable pageable);

  Page<OrderEntity> fetchOrdersByUserIdAndStatus(String userId, Pageable pageable,
                                           ExternalStatus... ois);

  OrderEntity fetchOrdersByUserIdAndCountryCodeAndOrderId(String userId, String countryCode, String orderId);

  void changeOrderState(OrderEntity order, String stateMachineType, String orderChangeEvent,
                        boolean changeOrderItemStatus, String reason);

  void recalculateOrder(OrderEntity order);

  void recalculateOrderForCustomerReplacementFlow(OrderEntity order, List<OrderItem> orderItemsEligibleForRecalculation);

  List<OrderItem> getOrderItemsForRecalculation(OrderEntity order);

  Map<String, Long> fetchOrderCountByStoreIdAndStatus(String storeId, List<ExternalStatus> status);

  Map<String, Long> fetchOrderCountByStoreIdAndStatusForDateRange(String storeId, long startTime, long endTime,
                                                                  List<ExternalStatus> status);

  OrderEntity findByTrackingId(String trackingId);

  Page<OrderEntity> fetchOrderByStoreIdForAllExternalStatus(String storeId, List<ExternalStatus> statusList, Pageable page);

  Page<OrderEntity> fetchOrderByStoreIdAndOrderId(String storeId, List<String> orderIds, Pageable page);

  Page<OrderEntity> fetchOrderByStoreIdForAllExternalStatusForDateRange(
      String storeId, Date startTime, Date endTime, List<ExternalStatus> externalStatusList, Pageable page);

  List<OrderEntity> findAllByOrderIdIn(List<String> orderIds);

  Map<Long, Number> fetchOrderCount(String countryCode, DashboardType dtype);

  Map<Long, Number> fetchOrderSales(String countryCode, DashboardType dtype);

  Long fetchOrderCountByUserId(String userId);

  Page<OrderEntity> fetchOrderByCountryCodeAndStatus(String countryCode,
                                                     List<OrderItemStatus> orderStatuses, Pageable page);

  Page<OrderEntity> fetchOrderByCountryCodeAndStatusAndUpdatedAtBefore(String countryCode,
                                                                 List<ExternalStatus> orderStatuses, Date date,
                                                                 Pageable page);

  Pair<Map<String, Long>, Map<String, Long>> fetchOrderCountByExternalStatus(String deliveryType, String countryCode,
                                                                             List<ExternalStatus> statuses);

  Pair<Map<String, Long>, Map<String, Long>> fetchOrderCountByExternalStatusAndMerchant(String deliveryType,
                                                                                        String countryCode,
                                                                                        List<String> storeIds,
                                                                                        List<ExternalStatus> statuses);

  Page<OrderEntity> fetchOrderByCountryCodeAndExternalStatus(String deliveryType, String countryCode,
                                                       List<ExternalStatus> externalStatusList,
                                                       Map<String, Long> externalStatusValues,
                                                       Pageable page);

  Page<OrderEntity> fetchOrderByCountryCodeAndExternalStatusAndMerchant(String deliveryType, String countryCode,
                                                                  List<String> storeIds,
                                                                  List<ExternalStatus> externalStatusList,
                                                                  Map<String, Long> externalStatusValues,
                                                                  Pageable page);

  List<OrderEntity> fetchOrderByStoreId(String storeId, Date startTime, Date endTime);

  List<OrderEntity> fetchOrderByStoreIdAndExternalStatus(String storeId, Date startTime, Date endTime,
                                                   List<ExternalStatus> externalStatuses);

  List<OrderEntity> fetchOrderByStoreIdAndExternalStatus(String storeId, ExternalStatus externalStatus);

  List<OrderEntity> fetchOrderByOrderPlacedTimeBetween(Date startTime, Date endTime);

  List<OrderEntity> fetchOrderByOrderPlacedTimeBetweenAndExternalStatus(Date startTime, Date endTime,
                                                                  List<ExternalStatus> externalStatuses);

  OrderEntity returnOrder(OrderEntity order, String reason, List<String> orderItemIds);

  OrderEntity fetchActiveOrdersByUserIdAndStatus(String userId, ExternalStatus[] ongoingStatus);

  List<OrderEntity> fetchOrderByUserIdAndExternalStatus(String userId, ExternalStatus externalStatus);

  boolean orderExistsByUserIdAndOrderIdAndExternalStatus(String userId, String orderId, ExternalStatus pendingApproval);

  List<OrderEntity> fetchOldOrdersForInvoiceRegeneration(Date startTime, Date endTime);

  void deleteUnusedOrder(String orderId);

  void processScheduledOrder();
}
