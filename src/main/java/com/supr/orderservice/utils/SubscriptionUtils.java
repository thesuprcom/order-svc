package com.supr.orderservice.utils;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.supr.orderservice.entity.SubscriptionTransactionEntity;
import com.supr.orderservice.enums.ErrorEnum;
import com.supr.orderservice.enums.PaymentMode;
import com.supr.orderservice.enums.PaymentStatus;
import com.supr.orderservice.model.PaymentProcessingResult;
import com.supr.orderservice.model.Result;
import com.supr.orderservice.model.response.PaymentGatewayResponse;
import com.supr.orderservice.model.response.PaymentOrderResponse;
import lombok.SneakyThrows;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.supr.orderservice.utils.Constants.INTERNAL_ACCESS_TOKEN_HEADER;
import static com.supr.orderservice.utils.Constants.PAYMENT_FAILED_ORDER_STATUS;
import static com.supr.orderservice.utils.Constants.PAYMENT_PENDING_ORDER_STATUS;
import static com.supr.orderservice.utils.Constants.SIGNATURE_HEADER;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class SubscriptionUtils {
  @SneakyThrows
  public static String createReturnUrl(String returnUrl, PaymentMode paymentMode, String countryCode) {
    return String.format(returnUrl, paymentMode, countryCode);
  }

  public static boolean isPaymentOrderFailedOrPending(final PaymentGatewayResponse noonPaymentResponse) {
    final String orderStatus = getOrderStatus(noonPaymentResponse);
    return orderStatus.equalsIgnoreCase(PAYMENT_FAILED_ORDER_STATUS) ||
        orderStatus.equalsIgnoreCase(PAYMENT_PENDING_ORDER_STATUS);
  }

  public static String getOrderStatus(final PaymentGatewayResponse noonResponse) {
    return Strings.nullToEmpty(Optional.ofNullable(noonResponse)
        .map(PaymentGatewayResponse::getResult)
        .map(Result::getOrder)
        .map(PaymentOrderResponse::getStatus)
        .orElse(null));
  }

  public static SubscriptionTransactionEntity buildSubscriptionTransactionEntity(final PaymentGatewayResponse noonPaymentResponse,
                                                                                 final String userId,
                                                                                 final PaymentOrderResponse order) {
    final SubscriptionTransactionEntity subscriptionTransactionEntity = SubscriptionTransactionEntity.builder()
        .amount(order.getAmount())
        .paymentGatewayResponse(noonPaymentResponse)
        .pgOrderId(getPgOrderId(noonPaymentResponse))
        .orderStatus(order.getStatus())
        .userId(userId)
        .build();
    return subscriptionTransactionEntity;
  }

  public static PaymentProcessingResult buildPaymentProcessingResult(final PaymentStatus paymentStatus) {
    return PaymentProcessingResult.builder().subscriptionStatus(paymentStatus).build();
  }

  public static PaymentProcessingResult buildPaymentProcessingResult(final PaymentStatus paymentStatus,
                                                                     final ErrorEnum subscriptionInitiationFailed) {
    return PaymentProcessingResult.builder()
        .subscriptionStatus(paymentStatus)
        .message(subscriptionInitiationFailed.getErrorMessage())
        .build();
  }

  public static String getRedirectUrl(final PaymentGatewayResponse noonPaymentResponse) {
    return Optional.ofNullable(noonPaymentResponse.getResult().getCheckoutData())
        .map(checkoutData -> checkoutData.getPostUrl())
        .orElse(null);
  }

  public static Long getPgOrderId(final PaymentGatewayResponse noonResponse) {
    return Optional.ofNullable(noonResponse)
        .map(PaymentGatewayResponse::getResult)
        .map(Result::getOrder)
        .map(PaymentOrderResponse::getId)
        .orElse(null);
  }

  public static String createSignature(String userId) {
    return BCrypt.hashpw(userId, BCrypt.gensalt());
  }

  public static boolean validateSignature(String userId, String signature) {
    return BCrypt.checkpw(userId, signature);
  }

  public static Map<String, String> createAuthHeadersForCloudTask(final String accessToken,
                                                                  final String userId) {
    Map<String, String> headers = Maps.newHashMap();
    headers.put(INTERNAL_ACCESS_TOKEN_HEADER, accessToken);
    headers.put(SIGNATURE_HEADER, createSignature(userId));
    headers.put(CONTENT_TYPE, APPLICATION_JSON_VALUE);
    return headers;
  }

}
