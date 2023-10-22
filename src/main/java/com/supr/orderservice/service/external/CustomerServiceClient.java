package com.supr.orderservice.service.external;

import com.supr.orderservice.model.DeliveryFeeInfo;
import com.supr.orderservice.model.request.UserDetailRequest;
import com.supr.orderservice.model.request.UserOtpRequest;
import com.supr.orderservice.model.request.UserOtpVerifyRequest;
import com.supr.orderservice.model.response.UserDetailResponse;
import com.supr.orderservice.model.response.VerifyGiftResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

import static com.supr.orderservice.utils.Constants.X_COUNTRY_CODE_HEADER_KEY;

@FeignClient(name = "user-service", url = "${user.base-url}")
public interface CustomerServiceClient {
  @DeleteMapping("/api/v1/cart/internal/clear/{orderId}")
  void clearCart(@PathVariable("orderId") String orderId);

  @PutMapping("/try-applying-referral-coupon/{user-id}")
  void tryApplyingReferralCoupon(@PathVariable("user-id") String userId);

  @GetMapping("/api/v1/internal/delivery-fee")
  DeliveryFeeInfo getDeliveryFeeInfo(@RequestHeader(value = X_COUNTRY_CODE_HEADER_KEY) final String countryCode,
                                     @RequestParam("total-offer-price") final BigDecimal totalOfferPrice);

  @PostMapping("/api/v1/user/auth/otp/")
  VerifyGiftResponse generateOtp(@RequestBody UserOtpRequest userOtpRequest);

  @PostMapping("/api/v1/user/verify/{request-type}/auth-otp/")
  VerifyGiftResponse verifyOtp(@RequestBody UserOtpVerifyRequest userOtpVerifyRequest,
                               @PathVariable("request-type") String requestType);

  @PutMapping("/api/v1/user/update/")
  UserDetailResponse updateUserDetails(@RequestBody UserDetailRequest userDetailRequest);
}
