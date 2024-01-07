package com.supr.orderservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public enum OrderItemStatus {
    CREATED,
    SENDER_PLACED,
    PAYMENT_LINK_CREATION_FAILED,
    PAYMENT_LINK_CREATION_SUCCESS,
    SENDER_PAYMENT_SUCCESS,
    RECEIVER_OPENED_GIFT,
    CHECKOUT,
    FAILED,
    PLACED,
    ACCEPTED,
    PROCESSING_ON_HOLD,
    PACKED_AT_SELLER,
    CANCELLED,
    CANCELLED_BY_SELLER,
    SHIPPED_TO_CUSTOMER,
    DELIVERED,
    PENDING_APPROVAL,
    REPLACEMENT_REQUESTED,
    GIFT_SWAPPED,
    PAYMENT_FAILED,
    ORDER_SCHEDULED,
    PARTIALLY_CANCELLED;
}
