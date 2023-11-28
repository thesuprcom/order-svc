package com.supr.orderservice.enums;

import java.util.Optional;

public enum TransactionStatus {
    CAPTURED,
    CREATED,
    AUTHORIZED,
    SUCCESSFUL,
    CREATE_PAYMENT_LINK,
    FAILED_TO_CREATE_PAYMENT_LINK,
    CREATE_SAVED_CARD_PAYMENT_LINK,
    SAVED_CARD_PAYMENT_SUCCESS,
    SAVED_CARD_PAYMENT_FAILED,
    FAILED_TO_CREATE_SAVED_CARD_PAYMENT_LINK,
    FAILED,
    INITIATED,
    REVERSED,
    REFUNDED,
    PARTIALLY_REFUNDED,
    FAILED_TO_REVERSE,
    FAILED_TO_CAPTURE,
    FAILED_TO_REFUND,
    FAILED_TO_PARTIALLY_REFUND,
    FAILED_TO_AUTHORIZE;
}
