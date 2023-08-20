package com.supr.orderservice.service.impl;

import com.supr.orderservice.entity.CouponInventoryUpdateFailureEntity;
import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.model.request.couponservice.UpdateCouponInventoryRequestDto;
import com.supr.orderservice.repository.CouponInventoryUpdateFailureRepository;
import com.supr.orderservice.service.CouponInventoryUpdateFailureService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CouponInventoryUpdateFailureServiceImpl implements CouponInventoryUpdateFailureService {

  private final CouponInventoryUpdateFailureRepository couponInventoryUpdateFailureRepository;

  @Override
  public void addCouponInventoryUpdateFailureData(final OrderEntity order,
                                                  final String couponId,
                                                  final UpdateCouponInventoryRequestDto updateCouponInventoryRequest,
                                                  final Exception exception) {
    final CouponInventoryUpdateFailureEntity couponInventoryUpdateFailureEntity =
        buildCouponInventoryUpdateFailureEntity(order, couponId, updateCouponInventoryRequest, exception);
    couponInventoryUpdateFailureRepository.save(couponInventoryUpdateFailureEntity);
  }

  private CouponInventoryUpdateFailureEntity buildCouponInventoryUpdateFailureEntity(
      final OrderEntity order, final String couponId, final UpdateCouponInventoryRequestDto updateCouponInventoryRequest,
      final Exception exception) {
    return CouponInventoryUpdateFailureEntity.builder()
        .couponId(couponId)
        .couponInventoryOperationType(updateCouponInventoryRequest.getCouponInventoryOperationType())
        .couponInventoryUpdateType(updateCouponInventoryRequest.getCouponInventoryUpdateType())
        .orderId(order.getOrderId())
        .updatedBy(updateCouponInventoryRequest.getUpdatedBy())
        .userId(updateCouponInventoryRequest.getUserId())
        .exceptionMessage(exception.getMessage()).build();
  }
}
