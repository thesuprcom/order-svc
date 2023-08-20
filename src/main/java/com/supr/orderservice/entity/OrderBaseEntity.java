package com.supr.orderservice.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.supr.orderservice.enums.ExternalStatus;
import com.supr.orderservice.enums.OrderItemStatus;
import lombok.Data;
import org.hibernate.envers.Audited;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

@Data
@Audited
@MappedSuperclass
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OrderBaseEntity extends BaseEntity {
    @Enumerated(EnumType.STRING)
    protected ExternalStatus externalStatus;

    @Enumerated(EnumType.STRING)
    protected OrderItemStatus status;

}
