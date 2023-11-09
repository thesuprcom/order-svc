package com.supr.orderservice.service.impl;

import com.supr.orderservice.entity.AccountingOrderItemEntity;
import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.entity.OrderItemStatusHistoryEntity;
import com.supr.orderservice.entity.OrderStatusHistoryEntity;
import com.supr.orderservice.enums.DashboardType;
import com.supr.orderservice.enums.ExternalStatus;
import com.supr.orderservice.enums.OrderChangeEvent;
import com.supr.orderservice.enums.OrderItemEvent;
import com.supr.orderservice.enums.OrderItemStatus;
import com.supr.orderservice.enums.OrderType;
import com.supr.orderservice.model.OrderItem;
import com.supr.orderservice.repository.OrderRepository;
import com.supr.orderservice.repository.ReceiverOrderRepository;
import com.supr.orderservice.repository.SenderOrderRepository;
import com.supr.orderservice.service.AccountingOrderItemDetailsService;
import com.supr.orderservice.service.OrderService;
import com.supr.orderservice.service.StateMachineManager;
import com.supr.orderservice.service.TransactionService;
import com.supr.orderservice.utils.ApplicationUtils;
import com.supr.orderservice.utils.Constants;
import com.supr.orderservice.utils.CouponUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final SenderOrderRepository senderOrderRepository;
    private final ReceiverOrderRepository receiverOrderRepository;
    private final StateMachineManager stateMachineManager;
    private final TransactionService transactionService;
    private final AccountingOrderItemDetailsService accountingOrderItemDetailsService;

    @Override
    public List<OrderEntity> findByOrderId(String orderId) {
        return orderRepository.findAllByOrderId(orderId);
    }

    @Override
    public OrderEntity fetchSenderOrder(String orderId) {
        return senderOrderRepository.findByOrderIdAndOrderType(orderId, OrderType.SENDER);
    }

    @Override
    public OrderEntity fetchReceiverOrder(String orderId) {
        return receiverOrderRepository.findByOrderIdAndOrderType(orderId, OrderType.RECEIVER);
    }

    @Override
    public OrderEntity fetchOrderForStore(String orderId, String storeId) {
        return null;
    }

    @Override
    public Page<OrderEntity> fetchOrdersByUserId(String id, String countryCode, Pageable pageable) {
        return null;
    }

    @Override
    public OrderEntity save(OrderEntity order) {
        return orderRepository.save(order);
    }

    @Override
    public OrderEntity cancelOrderEntity(OrderEntity order, String message, OrderItemEvent orderItemEvent, boolean shouldUpdateInventoryQty) {
        return null;
    }

    @Override
    public Page<OrderEntity> findByStoreId(String storeId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<OrderEntity> findByExternalStatusIn(List<ExternalStatus> externalStatuses, Pageable pageable) {
        return null;
    }

    @Override
    public Page<OrderEntity> fetchOrdersByUserIdAndStatus(String userId, Pageable pageable, ExternalStatus... ois) {
        return null;
    }

    @Override
    public OrderEntity fetchOrdersByUserIdAndCountryCodeAndOrderId(String userId, String countryCode, String orderId) {
        return null;
    }

    @Override
    public void changeOrderState(OrderEntity order, String stateMachineType, String orderChangeEvent,
                                 boolean changeOrderItemStatus, String reason) {

        final OrderItemStatus previousOrderStatus = order.getStatus();
        OrderStatusHistoryEntity historyVO = new OrderStatusHistoryEntity();
        historyVO.setActualTime(new Date());
        historyVO.setFromStatus(order.getStatus());
        historyVO.setOrder(order);
        historyVO.setReason(reason);
        stateMachineManager.moveToNextState(order, stateMachineType, orderChangeEvent);

        if (changeOrderItemStatus) {
            List<AccountingOrderItemEntity> accountingOrderItemDetailsList = new ArrayList<>();
            order.getOrderItemEntities().stream()
                    .filter(orderItem -> !ApplicationUtils.isInternalStatusCompleted(orderItem.getStatus()))
                    .forEach(orderItem -> {
                        OrderItemStatusHistoryEntity vo = new OrderItemStatusHistoryEntity();
                        vo.setFromStatus(orderItem.getStatus());
                        vo.setOrderItem(orderItem);
                        stateMachineManager.moveToNextState(orderItem, stateMachineType, orderChangeEvent);
                        vo.setToStatus(orderItem.getStatus());
                        vo.setReason(orderItem.getCancellationReason());
                        vo.setExternalStatus(orderItem.getExternalStatus());
                        updateUserDetailsForOrderItem(vo);
                        orderItem.getOrderItemStatusHistories().add(vo);
                        if (ApplicationUtils.isAccountingStatus(orderItem.getStatus())) {
                            accountingOrderItemDetailsList.add(AccountingOrderItemEntity.builder()
                                    .storeId(orderItem.getSellerId())
                                    .orderId(order.getOrderId())
                                    .shippingFee(null != order.getPrice() ? order.getPrice().getTotalShipping() : null)
                                    .orderItemId(orderItem.getOrderItemId())
                                    .status(orderItem.getStatus())
                                    .price(orderItem.getPrice())
                                    .quantity(orderItem.getMerchantAcceptedQuantity())
                                    .externalStatus(orderItem.getExternalStatus())
                                    .country(orderItem.getOrder().getShippingAddress().getCountry())
                                    .city(orderItem.getSellerInfo().getCity())
                                    .orderDate(orderItem.getOrder().getOrderPlacedTime())
                                    .nnOrderId(orderItem.getOrder().getId())
                                    .cancellationReason(orderItem.getCancellationReason())
                                    .itemInfo(null != orderItem.getItemInfo() ? orderItem.getItemInfo().getGiftTitle() : null)
                                    .couponIssuerType(CouponUtility.getCouponIssuerType(orderItem))
                                    .couponType(CouponUtility.getCouponType(orderItem.getCouponDetails()))
                                    .deliveryType(order.getDeliveryType())
                                    .build());
                        }
                    });
            if (!CollectionUtils.isEmpty(accountingOrderItemDetailsList)) {
                accountingOrderItemDetailsService.insertOrderItemList(accountingOrderItemDetailsList);
            }
        }

        historyVO.setToStatus(order.getStatus());
        historyVO.setExternalStatus(order.getExternalStatus());
        historyVO.setCompleted(ApplicationUtils.isInternalStatusCompleted(order.getStatus()));
        updateUserDetailsForOrder(historyVO);

        order.getOrderItemStatusHistories().add(historyVO);

        // Because, for RTO case, refund has already happened at cancel_by_logistics event.
        if (!previousOrderStatus.equals(OrderItemStatus.RTO_CREATED)) {
            transactionService.processPayment(order);
        }

    }

    @Override
    public void recalculateOrder(OrderEntity order) {

    }

    @Override
    public void recalculateOrderForCustomerReplacementFlow(OrderEntity order, List<OrderItem> orderItemsEligibleForRecalculation) {

    }

    @Override
    public List<OrderItem> getOrderItemsForRecalculation(OrderEntity order) {
        return null;
    }

    @Override
    public Map<String, Long> fetchOrderCountByStoreIdAndStatus(String storeId, List<ExternalStatus> status) {
        return null;
    }

    @Override
    public Map<String, Long> fetchOrderCountByStoreIdAndStatusForDateRange(String storeId, long startTime, long endTime, List<ExternalStatus> status) {
        return null;
    }

    @Override
    public OrderEntity findByTrackingId(String trackingId) {
        return null;
    }

    @Override
    public Page<OrderEntity> fetchOrderByStoreIdForAllExternalStatus(String storeId, List<ExternalStatus> statusList, Pageable page) {
        return null;
    }

    @Override
    public Page<OrderEntity> fetchOrderByStoreIdAndOrderId(String storeId, List<String> orderIds, Pageable page) {
        return null;
    }

    @Override
    public Page<OrderEntity> fetchOrderByStoreIdForAllExternalStatusForDateRange(String storeId, Date startTime, Date endTime, List<ExternalStatus> externalStatusList, Pageable page) {
        return null;
    }

    @Override
    public List<OrderEntity> findAllByOrderIdIn(List<String> orderIds) {
        return null;
    }

    @Override
    public Map<Long, Number> fetchOrderCount(String countryCode, DashboardType dtype) {
        return null;
    }

    @Override
    public Map<Long, Number> fetchOrderSales(String countryCode, DashboardType dtype) {
        return null;
    }

    @Override
    public Long fetchOrderCountByUserId(String userId) {
        return null;
    }

    @Override
    public Page<OrderEntity> fetchOrderByCountryCodeAndStatus(String countryCode, List<OrderItemStatus> orderStatuses, Pageable page) {
        return null;
    }

    @Override
    public Page<OrderEntity> fetchOrderByCountryCodeAndStatusAndUpdatedAtBefore(String countryCode, List<ExternalStatus> orderStatuses, Date date, Pageable page) {
        return null;
    }

    @Override
    public Pair<Map<String, Long>, Map<String, Long>> fetchOrderCountByExternalStatus(String deliveryType, String countryCode, List<ExternalStatus> statuses) {
        return null;
    }

    @Override
    public Pair<Map<String, Long>, Map<String, Long>> fetchOrderCountByExternalStatusAndMerchant(String deliveryType, String countryCode, List<String> storeIds, List<ExternalStatus> statuses) {
        return null;
    }

    @Override
    public Page<OrderEntity> fetchOrderByCountryCodeAndExternalStatus(String deliveryType, String countryCode, List<ExternalStatus> externalStatusList, Map<String, Long> externalStatusValues, Pageable page) {
        return null;
    }

    @Override
    public Page<OrderEntity> fetchOrderByCountryCodeAndExternalStatusAndMerchant(String deliveryType, String countryCode, List<String> storeIds, List<ExternalStatus> externalStatusList, Map<String, Long> externalStatusValues, Pageable page) {
        return null;
    }

    @Override
    public List<OrderEntity> fetchOrderByStoreId(String storeId, Date startTime, Date endTime) {
        return null;
    }

    @Override
    public List<OrderEntity> fetchOrderByStoreIdAndExternalStatus(String storeId, Date startTime, Date endTime, List<ExternalStatus> externalStatuses) {
        return null;
    }

    @Override
    public List<OrderEntity> fetchOrderByStoreIdAndExternalStatus(String storeId, ExternalStatus externalStatus) {
        return null;
    }

    @Override
    public List<OrderEntity> fetchOrderByOrderPlacedTimeBetween(Date startTime, Date endTime) {
        return null;
    }

    @Override
    public List<OrderEntity> fetchOrderByOrderPlacedTimeBetweenAndExternalStatus(Date startTime, Date endTime, List<ExternalStatus> externalStatuses) {
        return null;
    }

    @Override
    public OrderEntity returnOrder(OrderEntity order, String reason, List<String> orderItemIds) {
        return null;
    }

    @Override
    public OrderEntity fetchActiveOrdersByUserIdAndStatus(String userId, ExternalStatus[] ongoingStatus) {
        return null;
    }

    @Override
    public List<OrderEntity> fetchOrderByUserIdAndExternalStatus(String userId, ExternalStatus externalStatus) {
        return null;
    }

    @Override
    public boolean orderExistsByUserIdAndOrderIdAndExternalStatus(String userId, String orderId, ExternalStatus pendingApproval) {
        return false;
    }

    @Override
    public List<OrderEntity> fetchOldOrdersForInvoiceRegeneration(Date startTime, Date endTime) {
        return null;
    }

    @Override
    public void deleteUnusedOrder(String orderId) {

    }

    @Override
    public void processScheduledOrder() {
        log.info("Request to process the scheduled orders");
        long currentTimeMillis = System.currentTimeMillis();
        Timestamp currentTimestamp = new Timestamp(currentTimeMillis);
        List<OrderEntity> orderEntities =
                orderRepository.findAllByStatusAndScheduledDate(ExternalStatus.ORDER_SCHEDULED,
                currentTimestamp);
        orderEntities.forEach(orderEntity -> {
            log.info("Moving orderId :{} to Gift sent status form gift scheduled", orderEntity.getOrderId());
            stateMachineManager.moveToNextState(orderEntity, OrderType.SENDER.name(),
                    OrderChangeEvent.PROCESS_ORDER_SCHEDULED.name());
            orderEntity.getOrderItemEntities().forEach(orderItemEntity -> {
                stateMachineManager.moveToNextState(orderItemEntity, OrderType.SENDER.name(),
                        OrderChangeEvent.PROCESS_ORDER_SCHEDULED.name());
            });
        });
    }

    private void updateUserDetailsForOrderItem(OrderItemStatusHistoryEntity itemHistoryVO) {
        String userName = getUserName();
        if (Objects.nonNull(userName)) {
            itemHistoryVO.setUpdatedBy(userName);
        } else {
            itemHistoryVO.setUpdatedBy(Constants.ORDER_SERVICE);
        }
    }

    private String getUserName() {
        String userName = null;
        try {
            userName = SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (Exception e) {
            log.info("Exception in fetching user details to capture : {} :: {}", e.getMessage(),
                    Arrays.asList(e.getStackTrace()));
        }
        log.info("UserName obtained from jwt token : {}", userName);
        return userName;
    }

    private void updateUserDetailsForOrder(OrderStatusHistoryEntity historyVO) {
        String userName = getUserName();
        if (Objects.nonNull(userName)) {
            historyVO.setUpdatedBy(userName);
        } else {
            historyVO.setUpdatedBy(Constants.ORDER_SERVICE);
        }
    }

}
