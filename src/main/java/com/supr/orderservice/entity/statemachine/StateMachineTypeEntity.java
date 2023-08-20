package com.supr.orderservice.entity.statemachine;

import com.supr.orderservice.entity.BaseEntity;
import com.supr.orderservice.enums.EntityTypeEnum;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "state_machine_type")
public class StateMachineTypeEntity extends BaseEntity {
  @Column(name = "type")
  private String type;

  @Column(name = "entity_type")
  @Enumerated(value = EnumType.STRING)
  private EntityTypeEnum entityType;

  @Column(name = "description")
  private String description;
}
