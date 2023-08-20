package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SavedCardDetails {
  List<CardDetails> savedCards;
  CardDetails subscriptionDetails;
}
