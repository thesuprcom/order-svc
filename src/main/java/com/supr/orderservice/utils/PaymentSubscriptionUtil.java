package com.supr.orderservice.utils;

import com.google.common.base.Strings;
import com.supr.orderservice.entity.CardDetailsEntity;
import com.supr.orderservice.model.CardDetails;
import com.supr.orderservice.model.SavedCardDetails;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PaymentSubscriptionUtil {
  public static Optional<CardDetails> getSubscriptionDetails(final List<CardDetailsEntity> cardDetailsEntities,
                                                             final PaymentsUtil paymentsUtil) {
    return cardDetailsEntities.stream().findFirst()
        .map(cardDetailsEntity -> {
          Optional<CardDetails> optionalCardDetails = Optional.empty();

          if (!Strings.isNullOrEmpty(cardDetailsEntity.getSubscriptionId())) {
            optionalCardDetails = Optional.of(paymentsUtil.createCardDetails(cardDetailsEntity));
          }

          return optionalCardDetails;
        })
        .orElse(Optional.empty());

  }

  public static SavedCardDetails buildPaymentDetails(final List<CardDetails> cardDetails) {
    return SavedCardDetails.builder().savedCards(cardDetails).build();
  }

  public static SavedCardDetails buildPaymentDetails(final CardDetails subscriptionDetails) {
    return SavedCardDetails.builder().subscriptionDetails(subscriptionDetails).build();
  }

  public static List<CardDetails> getCardDetails(final List<CardDetailsEntity> cardDetailsEntities,
                                                 final PaymentsUtil paymentsUtil) {
    return cardDetailsEntities.stream().map(paymentsUtil::createCardDetails).collect(Collectors.toList());
  }
}
