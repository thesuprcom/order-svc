package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.supr.orderservice.enums.PaymentMode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CardDetails {
  private final String brand;
  private final String tokenId;
  private final String cardType;
  private final String expiryYear;
  private final String paymentInfo;
  private final String expiryMonth;
  private final String subscriptionId;
  private final PaymentMode paymentMode;
}
