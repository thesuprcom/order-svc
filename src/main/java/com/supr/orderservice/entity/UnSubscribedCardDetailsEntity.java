package com.supr.orderservice.entity;

import com.supr.orderservice.enums.PaymentMode;
import com.supr.orderservice.model.SubscriptionStatusDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.Version;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "unsubscribed_card_details")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnSubscribedCardDetailsEntity extends BaseEntity {
  @Version
  private Long version;

  @Getter
  @Setter
  @Column(name = "user_id")
  private String userId;

  @Getter
  @Setter
  @Column(name = "token_id")
  private String tokenId;

  @Getter
  @Setter
  @Column(name = "card_type")
  private String cardType;

  @Getter
  @Setter
  @Column(name = "brand")
  private String brand;

  @Getter
  @Setter
  @Column(name = "payment_info")
  private String paymentInfo;

  @Column(name = "expiry_month")
  private String expiryMonth;

  @Column(name = "expiry_year")
  private String expiryYear;

  @Getter
  @Setter
  @Column(name = "subscription_id")
  private String subscriptionId;

  @Getter
  @Setter
  @Enumerated(EnumType.STRING)
  @Column(name = "payment_mode")
  private PaymentMode paymentMode;

  @Getter
  @Setter
  @Type(type = "json")
  @Column(name = "subscription_status_details")
  private SubscriptionStatusDetails subscriptionStatusDetails;

  @Getter
  @Setter
  private String reason;
}
