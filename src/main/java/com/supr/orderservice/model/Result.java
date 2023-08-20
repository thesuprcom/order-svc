package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supr.orderservice.model.response.PaymentOrderResponse;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Result {
    private PaymentOrderResponse order;
    private Configuration configuration;
    private Business business;
    private CheckoutData checkoutData;
    private List<PaymentOption> paymentOptions = new ArrayList<>();
    private PaymentDetails paymentDetails;
    private List<PaymentGatewayTransaction> transactions;
    private PaymentGatewayTransaction transaction;
    private Subscription subscription;
}
