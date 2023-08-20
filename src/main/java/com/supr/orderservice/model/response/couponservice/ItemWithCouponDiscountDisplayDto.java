package com.supr.orderservice.model.response.couponservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemWithCouponDiscountDisplayDto {
  private String id;
  private BigDecimal salePricePerUnit;
  private BigDecimal quantity;
  private BigDecimal couponDiscountPerUnit;
  private BigDecimal merchantDiscountPerUnit;
}
