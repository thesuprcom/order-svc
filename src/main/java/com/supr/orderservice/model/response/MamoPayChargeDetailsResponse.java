package com.supr.orderservice.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.supr.orderservice.model.CustomerDetails;
import com.supr.orderservice.model.PaymentMethod;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MamoPayChargeDetailsResponse {
    private String status;
    private String id;
    private double amount;
    private String amountCurrency;
    private double refundAmount;
    private String refundStatus;
    private Map<String, String> customData;
    private String createdDate;
    private String subscriptionId;
    private String settlementAmount;
    private String settlementCurrency;
    private String settlementDate;
    private CustomerDetails customerDetails;
    private PaymentMethod paymentMethod;
    private String settlementFee;
    private String settlementVat;
    private String paymentLinkId;
    private String paymentLinkUrl;
    private String externalId;
    private String errorCode;
    private String errorMessage;
    private String nextPaymentDate;
}
