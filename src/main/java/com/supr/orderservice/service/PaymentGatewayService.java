package com.supr.orderservice.service;

import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.enums.PaymentMode;
import com.supr.orderservice.enums.SubscriptionPageContext;
import com.supr.orderservice.model.SavedCardDetails;
import com.supr.orderservice.model.request.PaymentRequest;
import com.supr.orderservice.model.request.SubscriptionRequest;
import com.supr.orderservice.model.response.PaymentGatewayResponse;
import com.supr.orderservice.model.response.PaymentProcessingResponse;


public interface PaymentGatewayService {
  SavedCardDetails paymentDetails(final String userId);

  PaymentProcessingResponse subscribe(SubscriptionRequest subscriptionRequest, String userId, String countryCode,
                                      final SubscriptionPageContext pageContext);

  void unsubscribe(String userId);

  void updateSubscription(final PaymentMode paymentMode, final String countryCode, Long pgOrderId);

  void capturePayment(PaymentRequest paymentRequest, final String accessToken, String signature);

  void refundPayment(PaymentRequest paymentRequest, String accessToken, String signature);

  void authorizePaymentForUserOrder(PaymentRequest paymentRequest, String accessToken, String signature);

  void unsubscribeUnusedCards();

  void deleteCardDetails(String userId);
  PaymentGatewayResponse enquireGateway(OrderEntity order);

  PaymentGatewayResponse capturePayment(OrderEntity order);

  PaymentGatewayResponse reversePayment(OrderEntity order);

  PaymentGatewayResponse refundPayment(OrderEntity order);

  PaymentGatewayResponse partialRefundPayment(OrderEntity order);

  PaymentGatewayResponse createPaymentLink(OrderEntity order);

  PaymentGatewayResponse createSavedCardPaymentLink(OrderEntity order);
}
