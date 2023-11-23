package com.supr.orderservice.enums;

import lombok.Getter;

@Getter
public enum ExternalStatus {
    GIFT_SCHEDULED("Gift Scheduled"),
    GIFT_PLACED("Gift order placed"),
    GIFT_CREATED("Gift Created"),
    GIFT_OPENED("Gift Opened"),
    GIFT_ACCEPTED("Gift Accepted"),
    GIFT_SWAPPED("Gift Swapped"),
    SHIPPED("Shipped"),
    PARTIALLY_SHIPPED("Partially Shipped"),
    DELIVERED("Delivered"),
    PARTIALLY_DELIVERED("Partially Delivered"),
    GIFT_UNDELIVERED("Gift Undelivered"),
    PAYMENT_FAILED("Payment Failed"),
    CANCELLED("Cancelled"),
    PROCESSING_ON_HOLD("Processing on hold"),
    PENDING("Merchant pending"),
    UNDELIVERED("Gift undelivered"),
    CANCELLED_BY_SELLER("Gift cancelled by seller"),
    EXPIRED("Expired");

    private String status;

    ExternalStatus(String value) {
        this.status = value;
    }
}
