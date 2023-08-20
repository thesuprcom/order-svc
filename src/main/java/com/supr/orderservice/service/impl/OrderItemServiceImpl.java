package com.supr.orderservice.service.impl;

import com.supr.orderservice.entity.OrderItemEntity;
import com.supr.orderservice.enums.OrderItemEvent;
import com.supr.orderservice.model.ItemInfo;
import com.supr.orderservice.model.OrderPrice;
import com.supr.orderservice.model.request.OrderItemReplacementRequest;
import com.supr.orderservice.service.OrderItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class OrderItemServiceImpl implements OrderItemService {
    @Override
    public OrderItemEntity save(OrderItemEntity orderItemVO) {
        return null;
    }

    @Override
    public List<OrderItemEntity> findAllByOrderIdIn(List<Long> orderIds) {
        return null;
    }

    @Override
    public List<OrderItemEntity> saveAll(List<OrderItemEntity> orderItems) {
        return null;
    }

    @Override
    public OrderItemEntity fetchByOrderItemId(String orderItemId) {
        return null;
    }

    @Override
    public void replaceOrderItem(OrderItemEntity orderItemToReplace, List<OrderItemReplacementRequest> replacementOrderItems) {

    }

    @Override
    public List<OrderItemEntity> fetchByOrderItemIdIn(List<String> orderItemIds) {
        return null;
    }

    @Override
    public void changeOrderItemStatus(OrderItemEntity orderItemVO, OrderItemEvent oie, String reason) {

    }

    @Override
    public OrderPrice recalculatePriceForAcceptedItem(OrderPrice originalPrice, ItemInfo item, BigDecimal itemPrice) {
        return null;
    }

    @Override
    public OrderItemEntity getReturnOrderItem(OrderItemEntity orderItemVO, BigDecimal quantity) {
        return null;
    }

    @Override
    public void deleteAllOrderItems(Long orderId) {

    }
}
