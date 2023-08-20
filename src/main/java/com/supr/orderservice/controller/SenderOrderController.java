package com.supr.orderservice.controller;

import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.enums.ExternalStatus;
import com.supr.orderservice.enums.OrderChangeEvent;
import com.supr.orderservice.enums.StateMachineType;
import com.supr.orderservice.exception.OrderServiceException;
import com.supr.orderservice.service.OrderService;
import com.supr.orderservice.service.StateMachineManager;
import com.supr.orderservice.service.WalletService;
import com.supr.orderservice.utils.ApplicationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/external/sender")
@RequiredArgsConstructor
public class SenderOrderController {
    private final OrderService orderService;
    private final WalletService walletService;
    private final StateMachineManager stateMachineManager;

    @DeleteMapping("/cancel/{order-id}")
    public ResponseEntity cancelSenderOrder(@PathVariable(name = "order-id") String orderId) {
        OrderEntity orderEntity = orderService.fetchSenderOrder(orderId);
        if (orderEntity == null) {
            throw new OrderServiceException("Invalid orderId into the request");
        }
        List<ExternalStatus> externalStatuses = ApplicationUtils.getOrderCancellationExternalStatus();
        if (externalStatuses.contains(orderEntity.getExternalStatus())) {
            stateMachineManager.moveToNextState(orderEntity, StateMachineType.SENDER.name(),
                    OrderChangeEvent.SENDER_CANCEL_ORDER.name());
            orderEntity.getOrderItemEntities().forEach(orderItemEntity ->
                    stateMachineManager.moveToNextState(orderItemEntity, StateMachineType.SENDER.name(),
                            OrderChangeEvent.SENDER_CANCEL_ORDER.name()));
            walletService.processRefund(orderEntity.getSender(), orderEntity.getTotalAmount(), orderEntity);
            return ResponseEntity.ok("Oder Cancelled!!");
        } else {
            throw new OrderServiceException("Order cannot be cancel at this stage");
        }
    }
}
