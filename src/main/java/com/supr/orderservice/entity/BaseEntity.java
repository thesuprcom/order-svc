package com.supr.orderservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.sql.Timestamp;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@TypeDef(name = "json", typeClass = JsonStringType.class)
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

  @Id
  @JsonIgnore
  @GenericGenerator(name = "native", strategy = "native")
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  public Long id;

  @CreatedDate
  @Column(name = "created_at")
  public Timestamp createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  public Timestamp updatedAt;
  
}
