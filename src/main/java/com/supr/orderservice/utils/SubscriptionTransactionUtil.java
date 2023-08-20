package com.supr.orderservice.utils;

import com.supr.orderservice.model.response.PaymentGatewayResponse;
import com.supr.orderservice.repository.SubscriptionTransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.supr.orderservice.utils.SubscriptionUtils.buildSubscriptionTransactionEntity;

@Slf4j
@Component
@AllArgsConstructor
public class SubscriptionTransactionUtil {
  private final SubscriptionTransactionRepository subscriptionTransactionRepository;

  public void saveResponse(final String userId, final PaymentGatewayResponse paymentGatewayResponse) {
    log.info("Customer initiated subscription response from noon payment: {} for user: {}", paymentGatewayResponse,
        userId);

    Optional.ofNullable(paymentGatewayResponse.getResult().getOrder())
        .ifPresent(order -> {
          var subscriptionTransactionEntity = buildSubscriptionTransactionEntity(paymentGatewayResponse, userId, order);
          subscriptionTransactionRepository.save(subscriptionTransactionEntity);
        });
  }
}
