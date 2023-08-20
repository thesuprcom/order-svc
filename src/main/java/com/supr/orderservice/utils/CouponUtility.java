package com.supr.orderservice.utils;

import com.supr.orderservice.entity.AccountingOrderItemEntity;
import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.entity.OrderItemEntity;
import com.supr.orderservice.enums.CouponInventoryUpdateType;
import com.supr.orderservice.enums.CouponIssuerType;
import com.supr.orderservice.enums.CouponType;
import com.supr.orderservice.enums.OrderItemEvent;
import com.supr.orderservice.enums.PaymentMode;
import com.supr.orderservice.model.CouponDetails;
import com.supr.orderservice.model.request.couponservice.ApplyOrderCouponRequestDto;
import com.supr.orderservice.model.request.couponservice.CouponOrderItem;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
public class CouponUtility {

  public CouponInventoryUpdateType getCouponInventoryUpdateType(final OrderItemEvent orderItemEvent,
                                                                final CouponDetails couponDetails) {
    final CouponIssuerType couponIssuerType = couponDetails.getCouponIssuerType();
    if (orderItemEvent == OrderItemEvent.CANCEL && couponIssuerType == CouponIssuerType.SUPR) {
      return CouponInventoryUpdateType.COUPON_LEVEL;
    }
    return CouponInventoryUpdateType.USER_LEVEL_AND_COUPON_LEVEL;
  }

  public ApplyOrderCouponRequestDto buildApplyCouponRequest(final List<OrderItemEntity> nonCancelledOrderItems) {
    return ApplyOrderCouponRequestDto.builder()
        .couponOrderItems(buildCouponOrderItems(nonCancelledOrderItems))
        .build();
  }

  private List<CouponOrderItem> buildCouponOrderItems(final List<OrderItemEntity> nonCancelledOrderItems) {
    return nonCancelledOrderItems.stream().map(CouponUtility::buildCouponOrderItem).collect(Collectors.toList());
  }

  private CouponOrderItem buildCouponOrderItem(final OrderItemEntity orderItemVO) {
    return CouponOrderItem.builder().id(orderItemVO.getOrderItemId())
        .quantity(orderItemVO.getOrderItemQuantity())
        .salePricePerUnit(orderItemVO.getPrice().getTotalOfferPrice()).build();
  }

  public boolean isCouponAppliedToOrder(final CouponDetails couponDetails) {
    return couponDetails != null;
  }

  public CouponIssuerType getCouponIssuerType(final AccountingOrderItemEntity accountingOrderItem) {
    return Optional.ofNullable(accountingOrderItem.getCouponIssuerType()).orElse(null);
  }

  public CouponIssuerType getCouponIssuerType(final OrderItemEntity orderItemVO) {
    return Optional.ofNullable(orderItemVO.getCouponDetails()).map(CouponDetails::getCouponIssuerType).orElse(null);
  }

  public boolean isInvalidPaymentOptionSelectedForCoupon(final CouponDetails couponDetails,
                                                         final PaymentMode paymentMode,
                                                         final String maskedCardNumber) {
    final boolean bankIssuedCoupon = CouponUtility.isBankIssuedCoupon(couponDetails);

    if (bankIssuedCoupon) {
      return CouponUtility.isBinMismatch(maskedCardNumber, couponDetails);
    }

    return false;
  }

  public CouponType getCouponType(CouponDetails couponDetails) {
    return Optional.ofNullable(couponDetails).map(CouponDetails::getCouponType)
        .orElse(null);
  }

  public boolean isBankIssuedCoupon(final CouponDetails couponDetails) {
    return Optional.ofNullable(couponDetails)
        .map(CouponDetails::getCouponIssuerType)
        .map(issuerType -> issuerType == CouponIssuerType.BANK)
        .orElse(false);
  }

  public boolean isBinMismatch(final String maskedCardNumber, final CouponDetails couponDetails) {
    final String firstSixDigitsOfCard = maskedCardNumber.substring(0, 6);
    return Optional.ofNullable(couponDetails)
        .map(CouponDetails::getBinValidationStrings)
        .map(binValidationStrings -> binValidationStrings.stream()
            .noneMatch(binValidationString -> binValidationString.equals(firstSixDigitsOfCard)))
        .orElse(false);
  }

  public boolean isCouponApplied(OrderEntity order) {
    return order.getCouponDetails() != null;
  }
}
