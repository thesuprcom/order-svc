package com.supr.orderservice.model.request.couponservice;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ApplyOrderCouponRequestDto {
  private List<CouponOrderItem> couponOrderItems;
}
