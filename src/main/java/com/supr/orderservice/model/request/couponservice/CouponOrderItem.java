package com.supr.orderservice.model.request.couponservice;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CouponOrderItem {
  private String id;
  private BigDecimal salePricePerUnit;
  private BigDecimal quantity;
}
