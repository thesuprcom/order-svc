package com.supr.orderservice.entity.statemachine;

import com.supr.orderservice.entity.BaseEntity;
import com.supr.orderservice.enums.ExternalStatus;
import com.supr.orderservice.enums.OrderItemStatus;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "state")
public class StateEntity extends BaseEntity {
  @Column(name = "system_internal_status")
  @Enumerated(value = EnumType.STRING)
  private OrderItemStatus systemInternalStatus;

  @Column(name = "user_visible_status")
  @Enumerated(value = EnumType.STRING)
  private ExternalStatus userVisibleStatus;
}