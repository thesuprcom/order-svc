package com.supr.orderservice.service;

import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.enums.CouponInventoryOperationType;
import com.supr.orderservice.enums.CouponInventoryUpdateType;
import com.supr.orderservice.model.CouponDetails;

public interface CouponInventoryManagementService {
  void updateCouponInventory(final OrderEntity order,
                             final CouponInventoryOperationType couponInventoryOperationType,
                             final CouponInventoryUpdateType couponInventoryUpdateType);

  void updateCouponInventory(final OrderEntity order,
                             final CouponDetails couponDetails,
                             final CouponInventoryOperationType couponInventoryOperationType,
                             final CouponInventoryUpdateType couponInventoryUpdateType);
}
