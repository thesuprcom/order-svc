package com.supr.orderservice.enums;

import java.util.Optional;

public enum TransactionStatus {
    CREATED,
    AUTHORIZED,
    SUCCESSFUL,
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
