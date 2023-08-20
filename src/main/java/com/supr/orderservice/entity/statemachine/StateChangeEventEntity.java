package com.supr.orderservice.entity.statemachine;

import com.supr.orderservice.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "state_change_event")
public class StateChangeEventEntity extends BaseEntity {
  @Column(name = "event")
  private String event;

  @Column(name = "should_send_email")
  private boolean shouldSendEmail;

  @Column(name = "should_send_sms")
  private boolean shouldSendSms;

  @Column(name = "description")
  private String description;
}
