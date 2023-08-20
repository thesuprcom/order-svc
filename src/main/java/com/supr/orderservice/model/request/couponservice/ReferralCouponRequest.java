package com.supr.orderservice.model.request.couponservice;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReferralCouponRequest {
  String userId;
  String countryCode;
  String createdBy;
}
