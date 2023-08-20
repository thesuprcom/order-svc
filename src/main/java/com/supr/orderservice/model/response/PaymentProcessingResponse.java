package com.supr.orderservice.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.supr.orderservice.model.PaymentProcessingResult;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PaymentProcessingResponse {
  String orderId;
  String returnUrl;
  String redirectUrl;
  Integer paymentTimeoutInSeconds;
  Integer orderStatusUpdateTimeoutInSeconds;
  PaymentProcessingResult paymentProcessingResult;
}
