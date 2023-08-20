package com.supr.orderservice.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DeliveryFeeInfo {
    private boolean isDeliveryFree;
    boolean isMandatoryDeliveryFee;
    private BigDecimal minValueForFreeDelivery;
    private BigDecimal moreValueForFreeDelivery;
    private BigDecimal totalDeliveryFee;
}