package com.supr.orderservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentData {
  private final String type;
  private final Object data;
}
