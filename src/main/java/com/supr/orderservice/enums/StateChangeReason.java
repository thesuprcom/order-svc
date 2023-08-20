package com.supr.orderservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StateChangeReason {
  PAYMENT_AUTHORIZATION_FAILED("Payment Failed"),
  PAYMENT_AUTHORIZATION_SUCCESSFULLY("Payment Successful");

  private final String reason;
}
