package com.supr.orderservice.utils;

import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.enums.OrderChangeEvent;
import com.supr.orderservice.enums.StateMachineType;
import com.supr.orderservice.service.StateMachineManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StateMachineUtils {
    private final StateMachineManager stateMachineManager;

    public void changeOrderAndOrderItemStatus(OrderEntity order, String stateMachineType,
                                              String stateChangeEvent) {
        stateMachineManager.moveToNextState(order, stateMachineType, stateChangeEvent);

        order.getOrderItemEntities().stream().forEach(orderItemEntity ->
                stateMachineManager.moveToNextState(orderItemEntity, stateMachineType, stateChangeEvent));
    }
}
