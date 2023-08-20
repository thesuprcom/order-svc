package com.supr.orderservice.service.external;

import com.supr.orderservice.model.request.couponservice.ApplyOrderCouponRequestDto;
import com.supr.orderservice.model.request.couponservice.ReferralCouponRequest;
import com.supr.orderservice.model.request.couponservice.UpdateCouponInventoryRequestDto;
import com.supr.orderservice.model.response.couponservice.Coupon;
import com.supr.orderservice.model.response.couponservice.OrderCouponDiscountResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = "coupon-service", url = "${coupon.base-url}")
public interface CouponServiceClient {

  @PutMapping("api/v1/internal/order-coupon-management/coupons/{couponId}/inventory")
  void updateCouponInventory(@PathVariable String couponId,
                             @RequestBody final UpdateCouponInventoryRequestDto updateCouponInventoryRequestDto);

  @GetMapping("api/v1/internal/order-coupon-management/coupons/{couponId}/discount")
  OrderCouponDiscountResponseDto fetchCouponDiscount(
      @PathVariable String couponId, @Valid @RequestBody final ApplyOrderCouponRequestDto applyOrderCouponRequestDto);

  @GetMapping("/coupon/referrer")
  Coupon createNewCouponForReferrer(@RequestBody ReferralCouponRequest referralCouponRequest);
}