package com.supr.orderservice.controller;

import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.entity.TransactionEntity;
import com.supr.orderservice.enums.OrderChangeEvent;
import com.supr.orderservice.enums.OrderType;
import com.supr.orderservice.enums.StateChangeReason;
import com.supr.orderservice.enums.StateMachineType;
import com.supr.orderservice.enums.TransactionStatus;
import com.supr.orderservice.exception.OrderServiceException;
import com.supr.orderservice.service.OrderInventoryManagementService;
import com.supr.orderservice.service.OrderService;
import com.supr.orderservice.utils.TokenUtility;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment-gateway")
public class MamoPaymentController {

    @Value("${order-secret-key}")
    private String secretKey;

    private final TokenUtility tokenUtility;
    private final OrderService orderService;
    private final OrderInventoryManagementService orderInventory;

    @PostMapping("webhook/{webhook-identifier}")
    public void handleMamoPayebhook(@PathVariable("webhook-identifier") String webhookIdentifier,
                                    @RequestBody String payload) {

    }

    @PostMapping("success")
    public void handlePaymentSuccess(@RequestParam("createdAt") String createAt,
                                     @RequestParam("paymentLinkId") String paymentLinkId,
                                     @RequestParam("status") String status,
                                     @RequestParam("token") String token,
                                     @RequestParam("user_id") String userId,
                                     @RequestParam("order_id") String orderId,
                                     @RequestParam("user_type") String userType,
                                     @RequestParam("transactionId") String transactionId) {
        log.info("Request from mamopay for payment failure for order: {} with pgOrderId: {} for orderType: {} with " +
                "pgStatus : {}", orderId, paymentLinkId, userType, status);
        OrderEntity order = validateRequest(token, paymentLinkId, orderId, userId, userType);
        TransactionEntity transaction = order.getTransaction();
        transaction.setTransactionId(transactionId);
        transaction.setPgOrderStatus(TransactionStatus.SUCCESSFUL);
        transaction.setPgOrderCreatedAt(createAt);
        order.setTransaction(transaction);
        order = orderService.save(order);
        StateMachineType stateMachineType = userType.equalsIgnoreCase(OrderType.SENDER.name()) ?
                StateMachineType.SENDER : StateMachineType.RECEIVER;
        orderService.changeOrderState(order, stateMachineType.name(), OrderChangeEvent.SENDER_PAYMENT_SUCCESS.name(), true,
                StateChangeReason.PAYMENT_SUCCESS.getReason());
        orderInventory.updateStoreOrderQuantity(order);

    }

    @PostMapping("failed")
    public void handlePaymentFailure(@RequestParam("createdAt") String createAt,
                                     @RequestParam("paymentLinkId") String paymentLinkId,
                                     @RequestParam("status") String status,
                                     @RequestParam("token") String token,
                                     @RequestParam("user_id") String userId,
                                     @RequestParam("order_id") String orderId,
                                     @RequestParam("user_type") String userType,
                                     @RequestParam("transactionId") String transactionId) {
        log.info("Request from mamopay for payment failure for order: {} with pgOrderId: {} for orderType: {} with " +
                        "pgStatus : {}", orderId, paymentLinkId, userType, status);
        OrderEntity order = validateRequest(token, paymentLinkId, orderId, userId, userType);
        TransactionEntity transaction = order.getTransaction();
        transaction.setTransactionId(transactionId);
        transaction.setPgOrderStatus(TransactionStatus.FAILED_TO_CAPTURE);
        transaction.setPgOrderCreatedAt(createAt);
        order.setTransaction(transaction);
        order = orderService.save(order);
        StateMachineType stateMachineType = userType.equalsIgnoreCase(OrderType.SENDER.name()) ?
                StateMachineType.SENDER : StateMachineType.RECEIVER;
        orderService.changeOrderState(order, stateMachineType.name(), OrderChangeEvent.SENDER_PAYMENT_FAILED.name(), true,
                StateChangeReason.PAYMENT_SUCCESS.getReason());
    }

    @SneakyThrows
    private OrderEntity validateRequest(String token, String paymentLinkId, String orderId, String userId, String userType) {
        String decryptedValue = tokenUtility.decryptValue(token, tokenUtility.generateSecretKey(secretKey));
        OrderEntity order;
        if (userType.equalsIgnoreCase(OrderType.SENDER.name())) {
            order = orderService.fetchSenderOrderWithStatus(orderId);
        } else {
            order = orderService.fetchReceiverOrderWithStatus(orderId);
        }
        if (order == null) {
            throw new OrderServiceException("Invalid request");
        }
        String decOrderId = decryptedValue.split("#")[0];
        String decUserId = decryptedValue.split("#")[1];
        if (!userId.equalsIgnoreCase(decUserId) && !decUserId.equalsIgnoreCase(order.getUserId())
                && !decOrderId.equalsIgnoreCase(orderId) && !decOrderId.equalsIgnoreCase(order.getOrderId())
                && !paymentLinkId.equalsIgnoreCase(order.getTransaction().getPgOrderIdentifier())) {
            throw new OrderServiceException("Invalid token into the request!!");
        }
        return order;
    }


}
