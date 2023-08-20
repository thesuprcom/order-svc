package com.supr.orderservice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public enum OrderItemStatus {
    CREATED(ExternalStatus.NOT_APPLICABLE, Optional.empty(), Optional.empty()),
    FAILED(ExternalStatus.FAILED, Optional.of(NotificationEventEnum.PAYMENT_FAILED), Optional.empty()),
    PLACED(ExternalStatus.PLACED, Optional.empty(), Optional.empty()),
    ACCEPTED(ExternalStatus.CONFIRMED, Optional.empty(), Optional.empty()),
    PROCESSING_ON_HOLD(ExternalStatus.PROCESSING_ON_HOLD, Optional.of(NotificationEventEnum.ORDER_MODIFIED),
            Optional.of(PaymentActionEnum.AUTHORIZE)),
    RTO_CREATED(ExternalStatus.RETURN_REQUESTED, Optional.empty(), Optional.empty()),
    PACKED_AT_SELLER(ExternalStatus.SHIPPED, Optional.empty(), Optional.empty()),
    CANCELLED(ExternalStatus.CANCELLED, Optional.empty(), Optional.empty()),
    CANCELLED_BY_SELLER(ExternalStatus.CANCELLED_BY_SELLER, Optional.empty(), Optional.empty()),
    SHIPPED_TO_CUSTOMER(ExternalStatus.SHIPPED, Optional.of(NotificationEventEnum.ORDER_PICKED), Optional.empty()),
    DELIVERED(ExternalStatus.DELIVERED, Optional.of(NotificationEventEnum.ORDER_DELIVERED), Optional.empty()),
    PENDING_APPROVAL(ExternalStatus.PENDING, Optional.of(NotificationEventEnum.ORDER_PENDING_APPROVAL),
            Optional.empty()),
    REPLACEMENT_REQUESTED(ExternalStatus.REPLACEMENT_IN_PROGRESS, Optional.empty(), Optional.empty()),
    GIFT_SWAPPED(ExternalStatus.GIFT_SWAPPED,Optional.empty(),Optional.empty()),
    ANY(ExternalStatus.NOT_APPLICABLE, Optional.empty(),Optional.empty()),
    ANOTHER_VARIANT(ExternalStatus.ANOTHER_VARIANT, Optional.empty(), Optional.empty()),
    ORDER_SCHEDULED(ExternalStatus.ORDER_SCHEDULED, Optional.empty(), Optional.empty());
    private final ExternalStatus externalStatus;
    private final Optional<NotificationEventEnum> notificationEventEnumOptional;
    private final Optional<PaymentActionEnum> paymentActionEnum;
}
