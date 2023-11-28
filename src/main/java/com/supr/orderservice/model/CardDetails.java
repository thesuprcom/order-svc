package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.supr.orderservice.enums.PaymentMode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardDetails {
  private final String brand;
  private final String tokenId;
  private final String cardType;
  private final String expiryYear;
  private final String paymentInfo;
  private final String expiryMonth;
  private final String subscriptionId;
  private String maskedCard;
  private  String cardId;
  private final PaymentMode paymentMode;
}
