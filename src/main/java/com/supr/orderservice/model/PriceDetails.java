package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PriceDetails implements Serializable {
    BigDecimal totalPrice;
    BigDecimal totalTax;
    BigDecimal totalDiscount;
    BigDecimal totalCouponDiscount;
    BigDecimal totalShipping;
}
