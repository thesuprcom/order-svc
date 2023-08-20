package com.supr.orderservice.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supr.orderservice.model.Result;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentGatewayResponse {
    private Integer resultCode;
    private String message;
    private Integer resultClass;
    private String classDescription;
    private String actionHint;
    private String requestReference;
    private Result result;
}
