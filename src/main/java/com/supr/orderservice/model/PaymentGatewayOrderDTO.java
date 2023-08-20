package com.supr.orderservice.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentGatewayOrderDTO {
  private Long id;
  private String name;
  private BigDecimal amount;
  private String currency;
  private String channel;
  private String category;
  private String reference;
  private String ipAddress;
}


