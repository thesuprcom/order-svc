package com.supr.orderservice.utils;

import com.google.common.collect.Iterables;
import com.supr.orderservice.entity.CardDetailsEntity;
import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.entity.PaymentDetailsEntity;
import com.supr.orderservice.enums.PaymentActionEnum;
import com.supr.orderservice.model.CardDetails;
import com.supr.orderservice.model.PaymentGatewayTransaction;
import com.supr.orderservice.model.response.PaymentGatewayResponse;
import com.supr.orderservice.model.response.PaymentOrderResponse;
import com.supr.orderservice.repository.PaymentDetailsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class PaymentsUtil {
  private final EncryptionUtil encryptionUtil;
  private final PaymentDetailsRepository paymentDetailsRepository;
  @Value("${payment.gateway.username}")
  private String username;
  @Value("${payment.gateway.password}")
  private String password;
  @Value("${payment.gateway.test-mode}")
  private String mode;

  public PaymentsUtil(EncryptionUtil encryptionUtil, PaymentDetailsRepository paymentDetailsRepository) {
    this.encryptionUtil = encryptionUtil;
    this.paymentDetailsRepository = paymentDetailsRepository;
  }

  public CardDetails createCardDetails(CardDetailsEntity cardDetailsEntity) {
    return CardDetails.builder()
        .brand(cardDetailsEntity.getBrand())
        .cardType(cardDetailsEntity.getCardType())
        .expiryMonth(cardDetailsEntity.getExpiryMonth(encryptionUtil))
        .expiryYear(cardDetailsEntity.getExpiryYear(encryptionUtil))
        .paymentInfo(cardDetailsEntity.getPaymentInfo())
        .tokenId(cardDetailsEntity.getTokenId())
        .subscriptionId(cardDetailsEntity.getSubscriptionId())
        .paymentMode(cardDetailsEntity.getPaymentMode())
        .build();
  }

  public String createAuthorizationToken() {
    return String.join("", "Key_", mode, " ",
        Base64.getEncoder().encodeToString(String.join(":", username, password).getBytes()));
  }

  /**
   * Saves the order details post every payment action.
   * BRD - https://docs.google.com/document/d/1SOOV0oMzHXPSSf3NzJjyze1H8DHLKaQe8bHQn5xPRJU/edit?ts=5ea94229#
   * @param order        The order placed by user
   * @param noonResponse The response from noon payments
   */
  public void trySavePaymentGatewayTransaction(OrderEntity order, PaymentGatewayResponse noonResponse) {
    PaymentOrderResponse paymentGatewayOrder = noonResponse.getResult().getOrder();
    final String orderStatus = paymentGatewayOrder.getStatus();

    if (!paymentDetailsRepository
        .existsByNnOrderIdAndPaymentOrderStatus(order.getId(), orderStatus)) {
      PaymentDetailsEntity paymentDetailsEntity = new PaymentDetailsEntity();
      paymentDetailsEntity.setNnOrderId(order.getId());
      paymentDetailsEntity.setAuthorizedAmount(paymentGatewayOrder.getTotalAuthorizedAmount());
      paymentDetailsEntity.setCapturedAmount(paymentGatewayOrder.getTotalSalesAmount());
      paymentDetailsEntity.setCurrency(paymentGatewayOrder.getCurrency());
      paymentDetailsEntity.setPaymentOrderStatus(orderStatus);
      paymentDetailsEntity.setErrorMessage(paymentGatewayOrder.getErrorMessage());

      Optional.ofNullable(noonResponse.getResult().getPaymentDetails()).ifPresent(paymentDetails -> {
        paymentDetailsEntity.setPaymentMethod(paymentDetails.getMode());
        paymentDetailsEntity.setCardCountry(paymentDetails.getCardCountry());
        paymentDetailsEntity.setCardBrand(paymentDetails.getBrand());
      });

      getPaymentGatewayTransaction(noonResponse).ifPresent(transaction -> {
        paymentDetailsEntity.setTransactionId(transaction.getId());
        paymentDetailsEntity.setTransactionStatus(transaction.getStatus());
        paymentDetailsEntity.setTransactionCreationTime(transaction.getCreationTime());
        paymentDetailsEntity.setTransactionAuthorizationCode(transaction.getAuthorizationCode());
      });

      paymentDetailsRepository.save(paymentDetailsEntity);
    } else {
      log.debug("Payment details are already saved for order {} and noon response {}", order, noonResponse);
    }
  }

  public boolean isPaymentAuthorized(final PaymentGatewayResponse response) {
    return Optional.ofNullable(response.getResult())
        .map(result -> Optional.ofNullable(result.getOrder())
            .map(order -> order.getStatus().equalsIgnoreCase(PaymentActionEnum.AUTHORIZE.getResponseOrderStatus()))
            .orElse(false))
        .orElse(false);
  }

  private Optional<PaymentGatewayTransaction> getPaymentGatewayTransaction(PaymentGatewayResponse noonResponse) {
    PaymentGatewayTransaction transaction = noonResponse.getResult().getTransaction();

    if (transaction == null) {
      List<PaymentGatewayTransaction> transactions = noonResponse.getResult().getTransactions();
      if (isPaymentGatewayTransactionDetailsAvailable(transactions)) {
        transaction = getRecentTransaction(transactions);
      }
    }

    return Optional.ofNullable(transaction);
  }

  private PaymentGatewayTransaction getRecentTransaction(List<PaymentGatewayTransaction> transactions) {
    return Iterables.getLast(transactions);
  }

  private boolean isPaymentGatewayTransactionDetailsAvailable(List<PaymentGatewayTransaction> transactions) {
    return !(transactions == null || transactions.isEmpty());
  }
}
