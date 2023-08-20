package com.supr.orderservice.model.response.couponservice;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class Coupon {
  String currency;
  String couponCode;
  BigDecimal maxDiscount;
  BigDecimal minCartValue;
  int couponValidityInDays;
  BigDecimal discountPercentage;
}
