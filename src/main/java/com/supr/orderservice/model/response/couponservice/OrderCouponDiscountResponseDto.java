package com.supr.orderservice.model.response.couponservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderCouponDiscountResponseDto {
  private String couponId;
  private BigDecimal totalCouponDiscount;
  private BigDecimal totalMerchantCouponDiscount;
  private String couponCode;
  private Map<String, ItemWithCouponDiscountDisplayDto> itemWithCouponDiscountDisplayDtoMap;
}
