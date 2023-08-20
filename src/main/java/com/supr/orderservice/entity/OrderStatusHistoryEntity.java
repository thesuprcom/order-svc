package com.supr.orderservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supr.orderservice.enums.ExternalStatus;
import com.supr.orderservice.enums.OrderItemStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity(name = "order_status_histories")
@Table(name = "order_status_histories")
@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({"hibernateLazyInitializer", "hibernate_lazy_initializer", "handler"})
public class OrderStatusHistoryEntity extends BaseEntity{
    @Enumerated(EnumType.STRING)
    private OrderItemStatus fromStatus;

    @Enumerated(EnumType.STRING)
    private OrderItemStatus toStatus;

    @Enumerated(EnumType.STRING)
    private ExternalStatus externalStatus;

    private boolean isCompleted;

    private String updatedBy;

    private String clientId;

    private String reason;

    private Date expectedAt;

    private Date actualTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @ToString.Exclude
    private OrderEntity order;
}
