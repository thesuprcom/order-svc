package com.supr.orderservice.service.impl;

import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.enums.CouponInventoryOperationType;
import com.supr.orderservice.enums.CouponInventoryUpdateType;
import com.supr.orderservice.model.CouponDetails;
import com.supr.orderservice.model.request.couponservice.UpdateCouponInventoryRequestDto;
import com.supr.orderservice.service.CouponInventoryManagementService;
import com.supr.orderservice.service.CouponInventoryUpdateFailureService;
import com.supr.orderservice.service.external.CouponServiceClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CouponInventoryManagementServiceImpl implements CouponInventoryManagementService {

  private final CouponServiceClient couponServiceClient;
  private final CouponInventoryUpdateFailureService couponInventoryUpdateFailureService;

  @Override
  public void updateCouponInventory(final OrderEntity order,
                                    final CouponInventoryOperationType couponInventoryOperationType,
                                    final CouponInventoryUpdateType couponInventoryUpdateType) {
    final CouponDetails couponDetails = order.getCouponDetails();
    final UpdateCouponInventoryRequestDto updateCouponInventoryRequest =
        buildUpdateCouponInventoryRequest(order.getUserId(), couponInventoryOperationType, couponInventoryUpdateType);
    updateCouponInventory(order, couponDetails.getCouponId(), updateCouponInventoryRequest);
  }

  @Override
  public void updateCouponInventory(final OrderEntity order, final CouponDetails couponDetails,
                                    final CouponInventoryOperationType couponInventoryOperationType,
                                    final CouponInventoryUpdateType couponInventoryUpdateType) {
    final UpdateCouponInventoryRequestDto updateCouponInventoryRequest =
        buildUpdateCouponInventoryRequest(order.getUserId(), couponInventoryOperationType, couponInventoryUpdateType);
    updateCouponInventory(order, couponDetails.getCouponId(), updateCouponInventoryRequest);
  }

  private void updateCouponInventory(final OrderEntity order, final String couponId,
                                     final UpdateCouponInventoryRequestDto updateCouponInventoryRequest) {
    try {
      couponServiceClient.updateCouponInventory(couponId, updateCouponInventoryRequest);
    } catch (Exception exception) {
      log.error("Exception occurred while updating coupon inventory. Order id: {} Request: {}, exception: {} ",
          order.getOrderId(), updateCouponInventoryRequest, exception);
      couponInventoryUpdateFailureService
          .addCouponInventoryUpdateFailureData(order, couponId, updateCouponInventoryRequest, exception);
      throw exception;
    }
  }

  private UpdateCouponInventoryRequestDto buildUpdateCouponInventoryRequest(
      final String userId, final CouponInventoryOperationType couponInventoryOperationType,
      final CouponInventoryUpdateType couponInventoryUpdateType) {
    return UpdateCouponInventoryRequestDto.builder()
        .couponInventoryOperationType(couponInventoryOperationType)
        .couponInventoryUpdateType(couponInventoryUpdateType)
        .userId(userId).build();
  }
}
