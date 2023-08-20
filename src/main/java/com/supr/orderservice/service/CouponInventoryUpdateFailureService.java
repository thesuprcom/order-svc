package com.supr.orderservice.service;

import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.model.request.couponservice.UpdateCouponInventoryRequestDto;

public interface CouponInventoryUpdateFailureService {
  void addCouponInventoryUpdateFailureData(OrderEntity order, String couponId,
                                           UpdateCouponInventoryRequestDto updateCouponInventoryRequest,
                                           Exception exception);
}
