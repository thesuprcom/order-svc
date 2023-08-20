package com.supr.orderservice.service.external;

import com.supr.orderservice.model.DeliveryFeeInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

import static com.supr.orderservice.utils.Constants.X_COUNTRY_CODE_HEADER_KEY;

@FeignClient(name = "cart-service", url = "${cart.base-url}")
public interface CartServiceClient {
  @DeleteMapping("/api/v1/cart/internal/clear/{orderId}")
  void clearCart(@PathVariable("orderId") String orderId);

  @PutMapping("/try-applying-referral-coupon/{user-id}")
  void tryApplyingReferralCoupon(@PathVariable("user-id") String userId);

  @GetMapping("/api/v1/internal/delivery-fee")
  DeliveryFeeInfo getDeliveryFeeInfo(@RequestHeader(value = X_COUNTRY_CODE_HEADER_KEY) final String countryCode,
                                     @RequestParam("total-offer-price") final BigDecimal totalOfferPrice);
}
