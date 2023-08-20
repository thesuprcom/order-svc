package com.supr.orderservice.model;

import com.supr.orderservice.enums.CouponIssuerType;
import com.supr.orderservice.enums.CouponType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponDetails {
    private String couponId;
    private String couponCode;
    private CouponType couponType;
    private BigDecimal couponDiscountValue;
    private CouponEligibility couponEligibility;
    private CouponIssuerType couponIssuerType;
    private Set<String> binValidationStrings;
}
