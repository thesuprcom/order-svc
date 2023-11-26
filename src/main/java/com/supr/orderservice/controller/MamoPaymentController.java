package com.supr.orderservice.controller;

import com.supr.orderservice.entity.CardDetailsEntity;
import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.entity.TransactionEntity;
import com.supr.orderservice.enums.OrderChangeEvent;
import com.supr.orderservice.enums.OrderType;
import com.supr.orderservice.enums.PaymentMode;
import com.supr.orderservice.enums.StateChangeReason;
import com.supr.orderservice.enums.StateMachineType;
import com.supr.orderservice.enums.TransactionStatus;
import com.supr.orderservice.exception.OrderServiceException;
import com.supr.orderservice.model.PaymentMethod;
import com.supr.orderservice.model.response.MamoPayChargeDetailsResponse;
import com.supr.orderservice.repository.CardDetailsRepository;
import com.supr.orderservice.service.OrderInventoryManagementService;
import com.supr.orderservice.service.OrderService;
import com.supr.orderservice.service.external.MamoPayServiceClient;
import com.supr.orderservice.utils.TokenUtility;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.supr.orderservice.utils.ApplicationUtils.generateCardId;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment-gateway")
public class MamoPaymentController {

    @Value("${order-secret-key}")
    private String secretKey;

    @Value("${payment.gateway.access-token}")
    private String mamoPayAccessToken;

    private final TokenUtility tokenUtility;
    private final OrderService orderService;
    private final MamoPayServiceClient mamoPayServiceClient;
    private final CardDetailsRepository cardDetailsRepository;
    private final OrderInventoryManagementService orderInventory;


    @PostMapping("webhook/{webhook-identifier}")
    public void handleMamoPayebhook(@PathVariable("webhook-identifier") String webhookIdentifier,
                                    @RequestBody String payload) {

    }

    @GetMapping("success")
    public void handlePaymentSuccess(@RequestParam("createdAt") String createAt,
                                     @RequestParam("chargeUID") String chargeUID,
                                     @RequestParam("paymentLinkId") String paymentLinkId,
                                     @RequestParam("status") String status,
                                     @RequestParam("token") String token,
                                     @RequestParam("user_id") String userId,
                                     @RequestParam("order_id") String orderId,
                                     @RequestParam("user_type") String userType,
                                     @RequestParam("transactionId") String transactionId) {
        log.info("Request from mamopay for payment failure for order: {} with pgOrderId: {} for orderType: {} with " +
                "pgStatus : {}", orderId, paymentLinkId, userType, status);
        MamoPayChargeDetailsResponse response = mamoPayServiceClient.fetchChargeDetails(chargeUID, mamoPayAccessToken);
        OrderEntity order = validateRequest(token, paymentLinkId, orderId, userId, userType);
        createCardDetails(response, order);
        TransactionEntity transaction = order.getTransaction();
        transaction.setPgTransactionId(transactionId);
        transaction.setChargeDetails(response);
        transaction.setPgOrderId(response.getId());
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

    @GetMapping("failed")
    public void handlePaymentFailure(@RequestParam("createdAt") String createAt,
                                     @RequestParam("chargeUID") String chargeUID,
                                     @RequestParam("paymentLinkId") String paymentLinkId,
                                     @RequestParam("status") String status,
                                     @RequestParam("token") String token,
                                     @RequestParam("user_id") String userId,
                                     @RequestParam("order_id") String orderId,
                                     @RequestParam("user_type") String userType,
                                     @RequestParam("transactionId") String transactionId) {
        log.info("Request from mamopay for payment failure for order: {} with pgOrderId: {} for orderType: {} with " +
                "pgStatus : {}", orderId, paymentLinkId, userType, status);
        MamoPayChargeDetailsResponse response = mamoPayServiceClient.fetchChargeDetails(chargeUID, mamoPayAccessToken);
        OrderEntity order = validateRequest(token, paymentLinkId, orderId, userId, userType);
        createCardDetails(response, order);
        TransactionEntity transaction = order.getTransaction();
        transaction.setTransactionId(transactionId);
        transaction.setChargeDetails(response);
        transaction.setPgOrderId(response.getId());
        transaction.setPgOrderStatus(TransactionStatus.FAILED_TO_CAPTURE);
        transaction.setPgOrderCreatedAt(createAt);
        order.setTransaction(transaction);
        order = orderService.save(order);
        StateMachineType stateMachineType = userType.equalsIgnoreCase(OrderType.SENDER.name()) ?
                StateMachineType.SENDER : StateMachineType.RECEIVER;
        orderService.changeOrderState(order, stateMachineType.name(), OrderChangeEvent.SENDER_PAYMENT_FAILED.name(), true,
                StateChangeReason.PAYMENT_SUCCESS.getReason());
    }

    private void createCardDetails(MamoPayChargeDetailsResponse response, OrderEntity order) {
        if (response.getPaymentMethod().getCardId() != null) {
            String tokenId = response.getPaymentMethod().getCardId();
            Optional<CardDetailsEntity> cardDetailsEntityOptional =
                    cardDetailsRepository.findByUserIdAndTokenId(order.getUserId(), tokenId);
            if (!cardDetailsEntityOptional.isPresent()) {
                CardDetailsEntity cardDetails = new CardDetailsEntity();
                PaymentMethod paymentMethod = response.getPaymentMethod();
                cardDetails.setCardType(paymentMethod.getType());
                cardDetails.setUserId(order.getUserId());
                cardDetails.setPaymentMode(PaymentMode.Card);
                cardDetails.setTokenId(paymentMethod.getCardLast4());
                cardDetails.setSubscriptionId(paymentMethod.getCardId());
                cardDetails.setBrand(paymentMethod.getOrigin());
                cardDetails.setCardId(generateCardId());
                cardDetailsRepository.save(cardDetails);
                log.info("Card details are saved for the userId: {}", order.getUserId());
            }
        }
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
