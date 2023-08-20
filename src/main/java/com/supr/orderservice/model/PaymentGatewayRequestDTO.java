package com.supr.orderservice.model;

import lombok.Data;

@Data
public class PaymentGatewayRequestDTO {
  private String apiOperation;
  private PaymentGatewayOrderDTO order;
  private Configuration configuration;
  private PgTransactionDTO transaction;
  private PaymentData paymentData;
  private Subscription subscription;
}
