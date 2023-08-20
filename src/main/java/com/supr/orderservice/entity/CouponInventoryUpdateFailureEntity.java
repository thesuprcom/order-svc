package com.supr.orderservice.entity;

import com.supr.orderservice.enums.CouponInventoryOperationType;
import com.supr.orderservice.enums.CouponInventoryUpdateType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "coupon_inventory_update_failure")
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponInventoryUpdateFailureEntity extends BaseEntity {
  @Column(name = "order_id")
  private String orderId;
  @Column(name = "coupon_id")
  private String couponId;
  @Column(name = "user_id")
  private String userId;
  @Enumerated(EnumType.STRING)
  @Column(name = "coupon_inventory_update_type")
  private CouponInventoryUpdateType couponInventoryUpdateType;
  @Enumerated(EnumType.STRING)
  @Column(name = "coupon_inventory_operation_type")
  private CouponInventoryOperationType couponInventoryOperationType;
  @Column(name = "coupon_inventory_operation_value")
  private long couponInventoryOperationValue;
  @Column(name = "updated_by")
  private String updatedBy;
  @Column(name = "exception_message")
  private String exceptionMessage;
}
