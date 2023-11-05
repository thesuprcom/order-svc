package com.supr.orderservice.service.impl;

import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.entity.OrderItemEntity;
import com.supr.orderservice.entity.OrderItemStatusHistoryEntity;
import com.supr.orderservice.enums.EntityTypeEnum;
import com.supr.orderservice.enums.ExternalStatus;
import com.supr.orderservice.exception.OrderServiceException;
import com.supr.orderservice.model.ItemChangeDto;
import com.supr.orderservice.model.ItemStatusChange;
import com.supr.orderservice.model.OrderCount;
import com.supr.orderservice.model.OrderPrice;
import com.supr.orderservice.model.PortalOrderDetail;
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
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
                                                      int days, Pageable pageable) {
        List<ExternalStatus> externalStatuses = ApplicationUtils.getSellerPortalExternalStatus();
        LocalDate startDate = LocalDate.now().minusDays(days);
        LocalDate endDate = LocalDate.now();
        List<Object[]> orderCountList = orderRepository.
                getOrderCountByStatusAndCountryCodeAndSellerIdAndDateGroupByStatus(externalStatuses, countryCode,
                        sellerId, startDate);
        List<OrderCount> orderCounts = new ArrayList<>();
        long totalOrderCount = 0;
        for (Object[] result : orderCountList) {
            String status = (String) result[0];
            Long count = (Long) result[1];
            orderCounts.add(new OrderCount(status, count));
            totalOrderCount = totalOrderCount + count;
        }
        Page<OrderEntity> orderEntities = orderRepository.
                findOrdersByStatusAndCountryCodeAndSellerIdAndBrandIdAndDateRange(ExternalStatus.PLACED,
                        countryCode, sellerId, startDate, endDate, pageable);
        List<PortalOrderDetail> portalOrderDetails = orderEntities.stream().map(PortalOrderDetail::new).toList();
        long totalOrderItems = portalOrderDetails.stream()
                .mapToInt(customObject -> customObject.getItemInfos().size()).sum();

        PortalOrderDetailListResponse response = new PortalOrderDetailListResponse();
        response.setOrderDetails(portalOrderDetails);
        response.setOrderCount(orderCounts);
        response.setNoOfDays(days);
        response.setTotalOrderCount(totalOrderCount);
        response.setTotalOrderItems(totalOrderItems);
        return response;
    }

    @Override
    public PortalOrderDetailResponse getOrderDetail(String orderId) {
        PortalOrderDetailResponse response = new PortalOrderDetailResponse();
        OrderEntity order = orderRepository.findByOrderId(orderId);
        if (order != null) {
            response.setPortalOrderDetail(new PortalOrderDetail(order));
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
            List<OrderItemEntity> orderItemEntities = order.getOrderItemEntities();
            if (request.isOrderLevelTracking() && request.getOrderTrackingInfo() != null) {
                order.setOrderTrackingInfo(request.getOrderTrackingInfo());
            }
            if (request.getItemChangeDto() != null && request.getItemChangeDto().size() > 0) {
                orderItemEntities = updateTheOrderItemsFromSeller(request.getItemChangeDto(), orderItemEntities, order);
            }
            if (request.getStatus() != null) {
                stateMachineManager.moveToNextState(order, EntityTypeEnum.ORDER.name(), request.getStatus());
                orderItemEntities.forEach(orderItemEntity -> {
                    stateMachineManager.moveToNextState(orderItemEntity, EntityTypeEnum.ORDER_ITEM.name(),
                            request.getStatus());
                    orderItemEntity.setUpdatedBy(request.getUpdatedBy());
                });
            }
            orderItemRepository.saveAll(orderItemEntities);
            order.setUpdatedBy(request.getUpdatedBy());
            OrderEntity savedOrderEntity = orderRepository.save(order);
            response.setPortalOrderDetail(new PortalOrderDetail(savedOrderEntity));
            return response;
        } else {
            throw new OrderServiceException("Invalid orderId in the request!!");
        }
    }

    @Override
    public PortalOrderDetailResponse markOrderShip(StatusChangeRequest request) {
        PortalOrderDetailResponse response = new PortalOrderDetailResponse();
        OrderEntity order = orderRepository.findByOrderId(request.getOrderId());
        if (order != null) {
            if (request.getOrderStatus() != null) {
                stateMachineManager.moveToNextState(order, EntityTypeEnum.ORDER.name(), request.getOrderStatus());
            }
            if (request.getItemStatusChanges() != null && request.getItemStatusChanges().size() > 0) {
                List<OrderItemEntity> orderItemEntities = order.getOrderItemEntities();
                Map<String, String> itemStatusChange =
                        request.getItemStatusChanges().stream().collect(Collectors
                                .toMap(ItemStatusChange::getPskuCode, ItemStatusChange::getStatus));
                orderItemEntities.forEach(orderItemEntity -> {
                    if (itemStatusChange.containsKey(orderItemEntity.getPskuCode())) {
                        stateMachineManager.moveToNextState(orderItemEntity, EntityTypeEnum.ORDER_ITEM.name(),
                                itemStatusChange.get(orderItemEntity.getPskuCode()));
                    }
                });
            }
            response.setPortalOrderDetail(new PortalOrderDetail(order));
            return response;
        } else {
            throw new OrderServiceException("Invalid orderId in the request!!");
        }
    }

    private List<OrderItemEntity> updateTheOrderItemsFromSeller(List<ItemChangeDto> itemChangeDto,
                                                                List<OrderItemEntity> orderItemEntities,
                                                                OrderEntity orderEntity) {
        List<OrderItemEntity> orderItemEntityList = new ArrayList<>();
        Map<String, ItemChangeDto> itemChangeDtoMap =
                itemChangeDto.stream().collect(Collectors.toMap(ItemChangeDto::getPskuCode,
                        Function.identity()));
        for (OrderItemEntity orderItem : orderItemEntities) {
            if (itemChangeDtoMap.containsKey(orderItem.getPskuCode())) {
                ItemChangeDto itemChange = itemChangeDtoMap.get(orderItem.getPskuCode());
                orderItem.setOrderItemQuantity(new BigDecimal(itemChange.getQuantity()));
                if (itemChange.getItemTrackingInfo() != null) {
                    orderItem.setItemTrackingInfo(itemChange.getItemTrackingInfo());
                }
            }
            orderItemEntityList.add(orderItem);
        }
        OrderPrice orderPrice = OrderUtils.reCalculatePrice(orderItemEntityList);
        orderEntity.setPrice(orderPrice);
        return orderItemEntityList;
    }

    @Override
    public PortalOrderStatusUpdatesResponse fetchStatusUpdates(String orderId) {
        PortalOrderStatusUpdatesResponse response = new PortalOrderStatusUpdatesResponse();
        List<OrderItemStatusHistoryEntity> orderItemStatus = new ArrayList<>();
        OrderEntity order = orderRepository.findByOrderId(orderId);
        if (order != null) {
            order.getOrderItemEntities().forEach(orderItemEntity -> {
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
                PortalOrderDetail orderDetail = new PortalOrderDetail(order);
                response.setOrderDetails(List.of(orderDetail));
                return response;
            } else {
                return new PortalOrderSearchResponse(Collections.emptyList());
            }
        }
        if (request.getStatus() != null) {
            List<OrderEntity> orderEntities =
                    orderRepository.findByStatusAndSellerId(OrderUtils.fetchExternalStatus(request.getStatus()),
                            request.getSellerId());
            List<PortalOrderDetail> portalOrderDetails = orderEntities.stream().map(PortalOrderDetail::new).toList();
            response.setOrderDetails(portalOrderDetails);
            return response;
        }
        return new PortalOrderSearchResponse(Collections.emptyList());
    }
}
