package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentGatewayTransaction {
    private String id;
    private String status;
    private String creationTime;
    private String authorizationCode;
}

