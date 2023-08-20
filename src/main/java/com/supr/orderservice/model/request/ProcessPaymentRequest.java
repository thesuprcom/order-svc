package com.supr.orderservice.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supr.orderservice.enums.PaymentMode;
import com.supr.orderservice.model.Address;
import com.supr.orderservice.model.CardData;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessPaymentRequest {
    @NotNull
    private PaymentMode paymentModeSelected;
    @NotNull
    private String orderId;
    private Address billingAddress;
    @Valid
    private CardData cardData;
}
