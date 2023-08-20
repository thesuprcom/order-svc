package com.supr.orderservice.model;

import com.supr.orderservice.enums.NewSubscriptionStatusEnum;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class NewSubscriptionStatus {
  NewSubscriptionStatusEnum newSubscriptionStatus;
  String subscriptionFailedErrorMessage;
  Integer timeoutInSeconds;
}
