package com.supr.orderservice.entity;

import com.supr.orderservice.model.response.PaymentGatewayResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "subscription_transactions")
public class SubscriptionTransactionEntity extends BaseEntity {
  @Column(name = "user_id")
  private String userId;

  @Column(name = "amount")
  private BigDecimal amount;

  @Column(name = "order_status")
  private String orderStatus;

  @Column(name = "pg_order_id")
  private String pgOrderId;

  @Type(type = "json")
  @Column(name = "payment_gateway_response")
  private PaymentGatewayResponse paymentGatewayResponse;
}
