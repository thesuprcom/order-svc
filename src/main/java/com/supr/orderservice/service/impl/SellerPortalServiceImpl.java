package com.supr.orderservice.service.impl;

import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.entity.OrderItemEntity;
import com.supr.orderservice.entity.OrderItemStatusHistoryEntity;
import com.supr.orderservice.enums.EntityTypeEnum;
import com.supr.orderservice.enums.ExternalStatus;
import com.supr.orderservice.enums.OrderChangeEvent;
import com.supr.orderservice.exception.OrderServiceException;
import com.supr.orderservice.model.ItemChangeDto;
import com.supr.orderservice.model.ItemStatusChange;
import com.supr.orderservice.model.OrderCount;
import com.supr.orderservice.model.OrderPrice;
import com.supr.orderservice.model.PortalOrderDetail;
import com.supr.orderservice.model.TrackingInfo;
import com.supr.orderservice.model.UserInfo;
import com.supr.orderservice.model.request.PortalUpdateOrderRequest;
import com.supr.orderservice.model.request.SearchOrderRequest;
import com.supr.orderservice.model.request.StatusChangeRequest;
import com.supr.orderservice.model.response.PortalOrderDetailListResponse;
import com.supr.orderservice.model.response.PortalOrderDetailResponse;
import com.supr.orderservice.model.response.PortalOrderSearchResponse;
import com.supr.orderservice.model.response.PortalOrderStatusUpdatesResponse;
import com.supr.orderservice.model.response.PortalUpdateOrderResponse;
import com.supr.orderservice.repository.OrderItemRepository;
import com.supr.orderservice.repository.OrderItemStatusHistoryRepository;
import com.supr.orderservice.repository.OrderRepository;
import com.supr.orderservice.service.SellerPortalService;
import com.supr.orderservice.service.StateMachineManager;
import com.supr.orderservice.utils.ApplicationUtils;
import com.supr.orderservice.utils.OrderUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SellerPortalServiceImpl implements SellerPortalService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final StateMachineManager stateMachineManager;
    private final OrderItemStatusHistoryRepository orderItemStatusHistoryRepository;

    @Override
    public PortalOrderDetailListResponse getOrderList(String countryCode, String sellerId, String brandCode,
                                                      String orderStatus, int days, Pageable pageable) {
        LocalDate startDate = LocalDate.now().minusDays(days);
        LocalDate endDate = LocalDate.now();
        Date convertedEndDate = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date convertedStartDate = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<ExternalStatus> externalStatuses = getExternalStatusList(orderStatus);

        List<Object[]> orderCountList = orderItemRepository.
                getOrderCountByStatusAndCountryCodeAndSellerIdAndBrandCodeAndDateGroupByStatus(brandCode, sellerId,
                        countryCode, convertedStartDate, externalStatuses);
        List<OrderCount> orderCounts = new ArrayList<>();
        long totalOrderCount = 0;
        for (Object[] result : orderCountList) {
            String status = ((ExternalStatus) result[0]).name();
            Long count = (Long) result[1];
            orderCounts.add(new OrderCount(status, count));
            totalOrderCount = totalOrderCount + count;
        }
        orderCounts.add(new OrderCount("ALL", totalOrderCount));
        Page<OrderItemEntity> orderEntitiesPage = orderItemRepository.
                findOrdersByCountryCodeAndSellerIdAndBrandCodeAndDateRange(brandCode, sellerId,
                        countryCode, convertedStartDate, convertedEndDate, pageable);
        Map<OrderEntity, List<OrderItemEntity>> orderEntityListMap = orderEntitiesPage.stream()
                .collect(Collectors.groupingBy(OrderItemEntity::getOrder));

        List<PortalOrderDetail> portalOrderDetails =
                orderEntityListMap.entrySet().stream().map(entry -> new PortalOrderDetail(entry.getKey(),
                        entry.getValue(), sellerId, brandCode)).toList();
        long completedCount = orderEntityListMap.values().stream()
                .flatMap(List::stream)
                .filter(orderItem -> "DELIVERED".equalsIgnoreCase(orderItem.getExternalStatus().getStatus()))
                .count();

        long returnedCount = orderEntityListMap.values().stream()
                .flatMap(List::stream)
                .filter(orderItem -> "GIFT_UNDELIVERED".equalsIgnoreCase(orderItem.getExternalStatus().getStatus()))
                .count();


        PortalOrderDetailListResponse response = new PortalOrderDetailListResponse();
        response.setOrderDetails(portalOrderDetails);
        response.setOrderCount(orderCounts);
        response.setNoOfDays(days);
        response.setTotalOrderCount(orderEntityListMap.size());
        response.setTotalReturnOrderCount(returnedCount);
        response.setTotalFulfilledOrderCount(completedCount);
        response.setTotalOrderItems(orderEntityListMap.values().stream().mapToInt(List::size).sum());
        return response;
    }

    private static List<ExternalStatus> getExternalStatusList(String orderStatus) {
        List<ExternalStatus> externalStatuses;
        if (orderStatus.equalsIgnoreCase("All")) {
            externalStatuses = ApplicationUtils.getSellerPortalExternalStatus();
        } else if (orderStatus.equalsIgnoreCase("Open")) {
            externalStatuses = Arrays.asList(ExternalStatus.GIFT_ACCEPTED, ExternalStatus.PENDING,
                    ExternalStatus.PROCESSING_ON_HOLD);
        } else if (orderStatus.equalsIgnoreCase("Shipped")) {
            externalStatuses = List.of(ExternalStatus.SHIPPED);
        } else if (orderStatus.equalsIgnoreCase("Closed")) {
            externalStatuses = Arrays.asList(ExternalStatus.DELIVERED, ExternalStatus.CANCELLED,
                    ExternalStatus.CANCELLED_BY_SELLER);
        } else if (orderStatus.equalsIgnoreCase("Queued")) {
            externalStatuses = Arrays.asList(ExternalStatus.GIFT_CREATED, ExternalStatus.GIFT_SWAPPED,
                    ExternalStatus.GIFT_PLACED, ExternalStatus.GIFT_SCHEDULED);
        } else if (orderStatus.equalsIgnoreCase("Cancelled")) {
            externalStatuses = List.of(ExternalStatus.CANCELLED_BY_SELLER);
        } else {
            externalStatuses = ApplicationUtils.getSellerPortalExternalStatus();
        }
        return externalStatuses;
    }

    @Override
    public PortalOrderDetailResponse getOrderDetail(String orderId, String sellerId, String brandCode) {
        PortalOrderDetailResponse response = new PortalOrderDetailResponse();
        OrderEntity order = orderRepository.findByOrderId(orderId);
        if (order != null) {
            List<OrderItemEntity> orderItemEntities =
                    order.getOrderItemEntities().stream().filter(orderItemEntity -> orderItemEntity.getSellerId().equalsIgnoreCase(sellerId)
                            && orderItemEntity.getBrandCode().equalsIgnoreCase(brandCode)).toList();
            response.setPortalOrderDetail(new PortalOrderDetail(order, orderItemEntities, sellerId, brandCode));
            return response;
        } else {
            throw new OrderServiceException("Invalid orderId in the request!!");
        }

    }

    @Override
    public PortalUpdateOrderResponse updateOrder(PortalUpdateOrderRequest request) {
        PortalUpdateOrderResponse response = new PortalUpdateOrderResponse();
        OrderEntity order = orderRepository.findByOrderId(request.getOrderId());
        if (order != null) {
            UserInfo receiver = order.getReceiver();
            if (request.getContactEmail() != null) {
                receiver.setEmailId(request.getContactEmail());
            }
            if (request.getContactPhone() != null) {
                receiver.setPhoneNumber(request.getContactPhone());
            }
            if (request.getShippingAddress() != null) {
                order.setShippingAddress(request.getShippingAddress());
            }
            if (request.getNotes() != null) {
                order.setNotes(request.getNotes());
            }
            order.setUpdatedBy(request.getUpdatedBy());
            List<OrderItemEntity> orderItemEntities =
                    order.getOrderItemEntities().stream().filter(orderItemEntity ->
                            orderItemEntity.getSellerId().equalsIgnoreCase(request.getSellerId())
                                    && orderItemEntity.getBrandCode().equalsIgnoreCase(request.getBrandCode())).toList();
            order.setUpdatedBy(request.getUpdatedBy());
            OrderEntity savedOrderEntity = orderRepository.save(order);
            response.setPortalOrderDetail(new PortalOrderDetail(savedOrderEntity,
                    orderItemEntities, request.getSellerId(), request.getBrandCode()));
            return response;
        } else {
            throw new OrderServiceException("Invalid orderId in the request!!");
        }
    }

    @Override
    public PortalOrderDetailResponse markOrderShipped(StatusChangeRequest request) {
        PortalOrderDetailResponse response = new PortalOrderDetailResponse();
        OrderEntity order = orderRepository.findByOrderId(request.getOrderId());
        Map<String, ItemStatusChange> itemStatusChangeMap =
                request.getItemUpdates().stream().collect(Collectors.toMap(ItemStatusChange::getOrderItemId,
                        Function.identity()));
        AtomicBoolean isPartialShipped = new AtomicBoolean(false);
        if (order != null) {
            order.setUpdatedBy(request.getUpdatedBy());
            order.getOrderItemEntities().forEach(orderItemEntity -> {
                ItemStatusChange itemStatusChange = itemStatusChangeMap.get(orderItemEntity.getOrderItemId());
                if (itemStatusChange.getQuantityShipped() < orderItemEntity.getOrderItemQuantity().intValue()) {
                    int remainQuantity =
                            orderItemEntity.getOrderItemQuantity().intValue() - itemStatusChange.getQuantityShipped();
                    orderItemEntity.setOrderItemQuantityShipped(orderItemEntity.getOrderItemQuantity());
                    orderItemEntity.setOrderItemQuantityCancelled(BigDecimal.ZERO);
                    orderItemEntity.setOrderItemQuantityRemaining(new BigDecimal(remainQuantity));
                    orderItemEntity.setUpdatedBy(request.getUpdatedBy());
                    TrackingInfo trackingInfo = request.isOrderLevelTracking() ? request.getOrderTrackingInfo() :
                            itemStatusChange.getItemTrackingInfo();
                    orderItemEntity.setItemTrackingInfo(trackingInfo);
                    orderItemRepository.save(orderItemEntity);
                    stateMachineManager.moveToNextState(orderItemEntity, EntityTypeEnum.ORDER_ITEM.name(),
                            OrderChangeEvent.GIFT_PARTIALLY_SHIPPED.name());
                    isPartialShipped.set(true);
                } else {
                    orderItemEntity.setOrderItemQuantityShipped(orderItemEntity.getOrderItemQuantity());
                    orderItemEntity.setOrderItemQuantityCancelled(BigDecimal.ZERO);
                    orderItemEntity.setOrderItemQuantityRemaining(BigDecimal.ZERO);
                    orderItemEntity.setUpdatedBy(request.getUpdatedBy());
                    TrackingInfo trackingInfo = request.isOrderLevelTracking() ? request.getOrderTrackingInfo() :
                            itemStatusChange.getItemTrackingInfo();
                    orderItemEntity.setItemTrackingInfo(trackingInfo);
                    stateMachineManager.moveToNextState(orderItemEntity, EntityTypeEnum.ORDER_ITEM.name(),
                            OrderChangeEvent.GIFT_SHIPPED.name());
                    orderItemRepository.save(orderItemEntity);
                }
            });
            if (isPartialShipped.get()) {
                stateMachineManager.moveToNextState(order, EntityTypeEnum.ORDER.name(), OrderChangeEvent.GIFT_PARTIALLY_SHIPPED.name());
            } else {
                stateMachineManager.moveToNextState(order, EntityTypeEnum.ORDER.name(), OrderChangeEvent.GIFT_SHIPPED.name());
            }

            List<OrderItemEntity> orderItemEntities =
                    order.getOrderItemEntities().stream().filter(orderItemEntity ->
                            orderItemEntity.getSellerId().equalsIgnoreCase(request.getSellerId())
                                    && orderItemEntity.getBrandCode().equalsIgnoreCase(request.getBrandCode())).toList();
            response.setPortalOrderDetail(new PortalOrderDetail(order, orderItemEntities,
                    request.getSellerId(), request.getBrandCode()));
            return response;
        } else {
            throw new OrderServiceException("Invalid orderId in the request!!");
        }
    }

    @Override
    public PortalOrderStatusUpdatesResponse fetchStatusUpdates(String orderId, String sellerId, String brandCode) {
        PortalOrderStatusUpdatesResponse response = new PortalOrderStatusUpdatesResponse();
        List<OrderItemStatusHistoryEntity> orderItemStatus = new ArrayList<>();
        OrderEntity order = orderRepository.findByOrderId(orderId);
        if (order != null) {
            List<OrderItemEntity> orderItemEntities =
                    order.getOrderItemEntities().stream().filter(orderItemEntity ->
                            orderItemEntity.getSellerId().equalsIgnoreCase(sellerId)
                                    && orderItemEntity.getBrandCode().equalsIgnoreCase(brandCode)).toList();
            orderItemEntities.forEach(orderItemEntity -> {
                List<OrderItemStatusHistoryEntity> itemStatusHistory = orderItemStatusHistoryRepository
                        .findOrderItemStatusHistoryEntityByOrderItem(orderItemEntity);
                orderItemStatus.addAll(itemStatusHistory);
            });
            response.setOrderItemStatus(orderItemStatus);
            return response;
        } else {
            throw new OrderServiceException("Invalid orderId in the request!!");
        }
    }

    @Override
    public PortalOrderSearchResponse searchOrder(SearchOrderRequest request) {
        PortalOrderSearchResponse response = new PortalOrderSearchResponse();
        if (request.getOrderId() != null && (request.getStatus() != null || request.getDeliveryMethod() != null)) {
            OrderEntity order = orderRepository.findByOrderId(request.getOrderId());
            if (order != null) {
                List<OrderItemEntity> orderItemEntities =
                        order.getOrderItemEntities().stream().filter(orderItemEntity ->
                                orderItemEntity.getSellerId().equalsIgnoreCase(request.getSellerId())
                                        && orderItemEntity.getBrandCode().equalsIgnoreCase(request.getBrandCode())).toList();
                PortalOrderDetail orderDetail = new PortalOrderDetail(order, orderItemEntities,
                        request.getSellerId(), request.getBrandCode());
                response.setOrderDetails(List.of(orderDetail));
                return response;
            } else {
                return new PortalOrderSearchResponse(Collections.emptyList());
            }
        }
        if (request.getStatus() != null) {
            List<OrderItemEntity> orderEntities =
                    orderItemRepository.findAllByExternalStatusAndSellerIdAndBrandCode(OrderUtils.fetchExternalStatus(request.getStatus()),
                            request.getSellerId(), request.getBrandCode());
            Map<OrderEntity, List<OrderItemEntity>> orderEntityListMap = orderEntities.stream()
                    .collect(Collectors.groupingBy(OrderItemEntity::getOrder));
            List<PortalOrderDetail> portalOrderDetails =
                    orderEntityListMap.entrySet().stream().map(order -> new PortalOrderDetail(order.getKey(),
                            order.getValue(), request.getSellerId(), request.getBrandCode())).toList();
            response.setOrderDetails(portalOrderDetails);
            return response;
        }
        return new PortalOrderSearchResponse(Collections.emptyList());
    }

}
