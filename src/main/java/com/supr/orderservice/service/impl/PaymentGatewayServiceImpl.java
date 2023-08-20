package com.supr.orderservice.service.impl;

import com.supr.orderservice.entity.CardDetailsEntity;
import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.enums.PaymentMode;
import com.supr.orderservice.enums.SubscriptionPageContext;
import com.supr.orderservice.model.SavedCardDetails;
import com.supr.orderservice.model.request.PaymentRequest;
import com.supr.orderservice.model.request.SubscriptionRequest;
import com.supr.orderservice.model.response.PaymentGatewayResponse;
import com.supr.orderservice.model.response.PaymentProcessingResponse;
import com.supr.orderservice.repository.CardDetailsRepository;
import com.supr.orderservice.service.PaymentGatewayService;
import com.supr.orderservice.utils.PaymentSubscriptionUtil;
import com.supr.orderservice.utils.PaymentsUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    public void updateSubscription(PaymentMode paymentMode, String countryCode, Long pgOrderId) {

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
}
