package com.supr.orderservice.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PgTransactionDTO {
  private BigDecimal amount;
  private String currency;
  private boolean finalCapture;
  private String transactionReference;
}
