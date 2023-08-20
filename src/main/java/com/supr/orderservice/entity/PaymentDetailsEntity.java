package com.supr.orderservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment_details")
public class PaymentDetailsEntity extends BaseEntity {
  @Column(name = "nn_order_id")
  private Long nnOrderId;

  @Column(name = "authorized_amount")
  private BigDecimal authorizedAmount;

  @Column(name = "captured_amount")
  private BigDecimal capturedAmount;

  @Column(name = "currency")
  private String currency;

  @Column(name = "payment_method")
  private String paymentMethod;

  @Column(name = "transaction_creation_time")
  private String transactionCreationTime;

  @Column(name = "transaction_id")
  private String transactionId;

  @Column(name = "transaction_status")
  private String transactionStatus;

  @Column(name = "transaction_authorization_code")
  private String transactionAuthorizationCode;

  @Column(name = "payment_order_status")
  private String paymentOrderStatus;

  @Column(name = "card_country")
  private String cardCountry;

  @Column(name = "card_brand")
  private String cardBrand;

  @Column(name = "error_message")
  private String errorMessage;
}
