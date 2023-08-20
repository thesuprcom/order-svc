package com.supr.orderservice.entity;


import com.supr.orderservice.enums.NewSubscriptionStatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Data
@Entity
@NoArgsConstructor
@Table(name = "user_subscription_status")
public class UserSubscriptionStatusEntity extends BaseEntity {
  @Column(name = "user_id")
  private String userId;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private NewSubscriptionStatusEnum status;
}
