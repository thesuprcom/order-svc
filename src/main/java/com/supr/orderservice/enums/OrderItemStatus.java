package com.supr.orderservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public enum OrderItemStatus {
    CREATED(ExternalStatus.GIFT_CREATED, Optional.empty(), Optional.empty()),
    SENDER_PLACED(ExternalStatus.GIFT_CREATED, Optional.empty(), Optional.empty()),
    CHECKOUT(ExternalStatus.GIFT_CREATED, Optional.empty(), Optional.empty()),
    FAILED(ExternalStatus.PAYMENT_FAILED, Optional.of(NotificationEventEnum.PAYMENT_FAILED), Optional.empty()),
    PLACED(ExternalStatus.PAYMENT_FAILED, Optional.empty(), Optional.empty()),
    ACCEPTED(ExternalStatus.GIFT_ACCEPTED, Optional.empty(), Optional.empty()),
    PROCESSING_ON_HOLD(ExternalStatus.PROCESSING_ON_HOLD, Optional.of(NotificationEventEnum.ORDER_MODIFIED),
            Optional.of(PaymentActionEnum.AUTHORIZE)),
    PACKED_AT_SELLER(ExternalStatus.SHIPPED, Optional.empty(), Optional.empty()),
    CANCELLED(ExternalStatus.CANCELLED, Optional.empty(), Optional.empty()),
    CANCELLED_BY_SELLER(ExternalStatus.CANCELLED, Optional.empty(), Optional.empty()),
    SHIPPED_TO_CUSTOMER(ExternalStatus.SHIPPED, Optional.of(NotificationEventEnum.ORDER_PICKED), Optional.empty()),
    DELIVERED(ExternalStatus.DELIVERED, Optional.of(NotificationEventEnum.ORDER_DELIVERED), Optional.empty()),
    PENDING_APPROVAL(ExternalStatus.PENDING, Optional.of(NotificationEventEnum.ORDER_PENDING_APPROVAL),
            Optional.empty()),
    REPLACEMENT_REQUESTED(ExternalStatus.GIFT_SWAPPED, Optional.empty(), Optional.empty()),
    GIFT_SWAPPED(ExternalStatus.GIFT_SWAPPED,Optional.empty(),Optional.empty()),
    ORDER_SCHEDULED(ExternalStatus.GIFT_SCHEDULED, Optional.empty(), Optional.empty());
    private final ExternalStatus externalStatus;
    private final Optional<NotificationEventEnum> notificationEventEnumOptional;
    private final Optional<PaymentActionEnum> paymentActionEnum;
}
