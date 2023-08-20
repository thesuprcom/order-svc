package com.supr.orderservice.model.request.couponservice;

import com.supr.orderservice.enums.CouponInventoryOperationType;
import com.supr.orderservice.enums.CouponInventoryUpdateType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import static com.supr.orderservice.utils.Constants.ORDER_SERVICE;


@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateCouponInventoryRequestDto {
  String userId;
  CouponInventoryUpdateType couponInventoryUpdateType;
  CouponInventoryOperationType couponInventoryOperationType;
  String updatedBy = ORDER_SERVICE;
}
