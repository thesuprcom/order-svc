package com.supr.orderservice.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supr.orderservice.model.CardData;
import lombok.Builder;
import lombok.Value;

import javax.validation.Valid;

@Value
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriptionRequest {
  @Valid
  CardData cardData;

}
