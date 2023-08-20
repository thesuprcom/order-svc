package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentDetails {
    private String tokenIdentifier;
    private String cardType;
    private String brand;
    private String paymentInfo;
    private String expiryMonth;
    private String expiryYear;
    private String mode;
    private String cardCountry;
}
