package com.supr.orderservice.model;

import com.supr.orderservice.enums.SubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class SubscriptionStatusDetails {
    private SubscriptionStatus subscriptionStatus;
    private Set<String> paymentPendingOrder;
}
