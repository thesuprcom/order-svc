package com.supr.orderservice.entity.statemachine;

import com.supr.orderservice.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "state_machine")
public class StateMachineEntity extends BaseEntity {
  @ManyToOne
  @JoinColumn(name = "state_machine_type_id")
  private StateMachineTypeEntity stateMachineTypeEntity;

  @ManyToOne
  @JoinColumn(name = "from_state_id")
  private StateEntity fromState;

  @ManyToOne
  @JoinColumn(name = "to_state_id")
  private StateEntity toState;

  @ManyToOne
  @JoinColumn(name = "state_change_event_id")
  private StateChangeEventEntity stateChangeEventEntity;

  @Column(name = "is_active")
  private boolean isActive = true;
}