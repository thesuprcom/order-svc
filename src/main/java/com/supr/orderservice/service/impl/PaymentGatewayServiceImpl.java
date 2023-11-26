package com.supr.orderservice.service.impl;

import com.google.gson.Gson;
import com.supr.orderservice.entity.CardDetailsEntity;
import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.entity.TransactionEntity;
import com.supr.orderservice.enums.ErrorEnum;
import com.supr.orderservice.enums.PaymentMode;
import com.supr.orderservice.enums.SubscriptionPageContext;
import com.supr.orderservice.enums.TransactionStatus;
import com.supr.orderservice.exception.OrderServiceException;
import com.supr.orderservice.model.SavedCardDetails;
import com.supr.orderservice.model.UserInfo;
import com.supr.orderservice.model.pg.request.MamoPayPaymentLinkRequest;
import com.supr.orderservice.model.pg.response.MamoPayPaymentLinkResponse;
import com.supr.orderservice.model.request.PaymentGatewayRequest;
import com.supr.orderservice.model.request.PaymentRequest;
import com.supr.orderservice.model.request.SubscriptionRequest;
import com.supr.orderservice.model.response.PaymentGatewayResponse;
import com.supr.orderservice.model.response.PaymentProcessingResponse;
import com.supr.orderservice.repository.CardDetailsRepository;
import com.supr.orderservice.service.PaymentGatewayService;
import com.supr.orderservice.service.external.MamoPayServiceClient;
import com.supr.orderservice.utils.PaymentSubscriptionUtil;
import com.supr.orderservice.utils.PaymentsUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

import static com.supr.orderservice.utils.PaymentSubscriptionUtil.buildPaymentDetails;
import static com.supr.orderservice.utils.PaymentSubscriptionUtil.getCardDetails;
import static com.supr.orderservice.utils.PaymentSubscriptionUtil.getSubscriptionDetails;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentGatewayServiceImpl implements PaymentGatewayService {
    private final PaymentsUtil paymentsUtil;
    private final CardDetailsRepository cardDetailsRepository;
    private final MamoPayServiceClient mamoPayServiceClient;
    private final Gson gson;

    @Value("${processing-fee-percentage}")
    private int processingFeePercentage;

    @Value("${payment.gateway.access-token}")
    private String mamoPayAccessToken;

    @Override
    public SavedCardDetails paymentDetails(String userId) {
        final List<CardDetailsEntity> cardDetailsEntities = cardDetailsRepository.findByUserId(userId);
        return getSubscriptionDetails(cardDetailsEntities, paymentsUtil)
                .map(PaymentSubscriptionUtil::buildPaymentDetails)
                .orElse(buildPaymentDetails(getCardDetails(cardDetailsEntities, paymentsUtil)));
    }

    @Override
    public PaymentProcessingResponse subscribe(SubscriptionRequest subscriptionRequest, String userId,
                                               String countryCode, SubscriptionPageContext pageContext) {
        return null;
    }

    @Override
    public void unsubscribe(String userId) {

    }

    @Override
    public void updateSubscription(PaymentMode paymentMode, String countryCode, String pgOrderId) {

    }

    @Override
    public void capturePayment(PaymentRequest paymentRequest, String accessToken, String signature) {

    }

    @Override
    public void refundPayment(PaymentRequest paymentRequest, String accessToken, String signature) {

    }

    @Override
    public void authorizePaymentForUserOrder(PaymentRequest paymentRequest, String accessToken, String signature) {

    }

    @Override
    public void unsubscribeUnusedCards() {

    }

    @Override
    public void deleteCardDetails(String userId) {

    }

    @Override
    public PaymentGatewayResponse enquireGateway(OrderEntity order) {
        return null;
    }

    @Override
    public PaymentGatewayResponse capturePayment(OrderEntity order) {
        return null;
    }

    @Override
    public PaymentGatewayResponse reversePayment(OrderEntity order) {
        return null;
    }

    @Override
    public PaymentGatewayResponse refundPayment(OrderEntity order) {
        return null;
    }

    @Override
    public PaymentGatewayResponse partialRefundPayment(OrderEntity order) {
        return null;
    }

    @Override
    public PaymentGatewayResponse createPaymentLink(OrderEntity order) {
        PaymentGatewayResponse paymentGatewayResponse = new PaymentGatewayResponse();
        TransactionEntity transaction = order.getTransaction();
        log.info("Request to call payment gateway for order Id: {}", order.getOrderId());
        MamoPayPaymentLinkRequest mamoPayPaymentLinkRequest = createMamoPayLinkRequest(order);
        transaction.setPaymentGatewayRequest(new PaymentGatewayRequest(mamoPayPaymentLinkRequest));
        log.info("Payment Link request: {}", mamoPayPaymentLinkRequest);
        try {
            MamoPayPaymentLinkResponse response = mamoPayServiceClient.createPaymentLink(mamoPayPaymentLinkRequest,
                    mamoPayAccessToken);
            log.info("Payment gateway Call is success for order id: {}", order.getOrderId());
            paymentGatewayResponse.setResponse(response);
            transaction.setPgOrderStatus(TransactionStatus.CREATE_PAYMENT_LINK);
            transaction.setPaymentLink(response.getPaymentUrl());
            transaction.setPgOrderIdentifier(response.getId());

        } catch (Exception exception) {
            log.error("Error while calling the mamopay pg for order : {} Exception : {} ", order.getOrderId(),
                    exception);
            transaction.setPgOrderStatus(TransactionStatus.FAILED_TO_CREATE_PAYMENT_LINK);
            transaction.setFailureReason(exception.getMessage());
        }
        return paymentGatewayResponse;
    }

    @Override
    public PaymentGatewayResponse createSavedCardPaymentLink(OrderEntity order) {
        return null;
    }

    private MamoPayPaymentLinkRequest createMamoPayLinkRequest(OrderEntity order) {
        MamoPayPaymentLinkRequest request = new MamoPayPaymentLinkRequest();
        request.setFirstName(order.getSender() != null ? order.getSender().getFirstName() : Strings.EMPTY);
        request.setLastName(order.getSender() != null ? order.getSender().getLastName() : Strings.EMPTY);
        request.setEmail(order.getSender() != null ? order.getSender().getEmailId() : Strings.EMPTY);
        request.setExternalId(order.getOrderId());
        HashMap<String, String> customerData = new HashMap<>();
        customerData.put("billing_address", gson.toJson(order.getBillingAddress()));
        customerData.put("user_id", order.getUserId());
        request.setCustomData(customerData);
        request.setCapacity(1);
        request.setSendCustomerReceipt(false);
        request.setEnableQrCode(false);
        request.setEnableQuantity(false);
        request.setEnableCustomerDetails(true);
        request.setSaveCard("optional");
        request.setEnableTips(true);
        request.setEnableMessage(true);
        request.setEnableTabby(true);
        request.setWidget(true);
        request.setAmountCurrency(fetchAmountCurrency(order.getCurrencyCode()));
        request.setAmount(order.getPrice().getTotalPrice().doubleValue());
        request.setProcessingFeePercentage(processingFeePercentage);
        request.setFailureReturnUrl("https://myawesomewebsite.com/failed");
        request.setReturnUrl("https://myawesomewebsite.com/paymentSuccess");
        request.setActive(true);
        request.setTitle("Payment for orderId: " + order.getOrderId());
        UserInfo receiver = order.getReceiver();
        String name = "User";
        if (receiver != null) {
            name = request.getFirstName() + " " + receiver.getLastName();
        }
        request.setDescription("Sending gifts to " + name);
        return request;
    }

    private String fetchAmountCurrency(String currencyCode) {
        if(currencyCode.equalsIgnoreCase("ae"))
            return "AED";
        else
            return "AED";
    }

}
