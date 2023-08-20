package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OrderItemPrice {
    private BigDecimal mrp;
    private BigDecimal offerPrice;
    private BigDecimal discount;
    private BigDecimal shippingFees;
    private BigDecimal giftCharge;
    private BigDecimal sellingPrice;
    private BigDecimal landingCost;
}
