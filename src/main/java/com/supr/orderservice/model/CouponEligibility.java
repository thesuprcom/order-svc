package com.supr.orderservice.model;

import com.supr.orderservice.enums.CouponEligibilityStatus;
import com.supr.orderservice.enums.CouponIneligibilityType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CouponEligibility {
    private CouponEligibilityStatus couponEligibilityStatus;
    private CouponIneligibilityType couponIneligibilityType;
    private String couponIneligibilityMessage;
}