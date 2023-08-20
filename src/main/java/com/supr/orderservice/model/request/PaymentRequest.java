package com.supr.orderservice.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.supr.orderservice.enums.NextAction;
import com.supr.orderservice.enums.PaymentMode;
import com.supr.orderservice.enums.PaymentReason;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentRequest {
  String userId;
  String orderId;
  Long pgOrderId;
  String currency;
  BigDecimal amount;
  PaymentMode paymentMode;
  PaymentReason paymentReason;
  List<NextAction> nextActions;
}
