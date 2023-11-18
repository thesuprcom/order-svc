package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.supr.orderservice.enums.PaymentStatus;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PaymentProcessingResult {
  String message;
  PaymentStatus paymentStatus;
}
