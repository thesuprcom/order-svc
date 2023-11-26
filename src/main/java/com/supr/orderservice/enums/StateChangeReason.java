package com.supr.orderservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StateChangeReason {
  PAYMENT_LINK_CREATED_FAILED("Payment link creation Failed"),
  PAYMENT_LINK_CREATED_SUCCESSFULLY("Payment link created successfully"),
  PAYMENT_SUCCESS("Order Payment is success"),
  ORDER_PLACED("Order placed");


  private final String reason;
}
