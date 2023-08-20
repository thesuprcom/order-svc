package com.supr.orderservice.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentOrderResponse {
    private String status;
    private String creationTime;
    private Long id;
    private BigDecimal amount;
    private BigDecimal totalAuthorizedAmount;
    private BigDecimal totalSalesAmount;
    private BigDecimal totalCapturedAmount;
    private BigDecimal totalRefundedAmount;
    private BigDecimal totalReversedAmount;
    private String currency;
    private String name;
    private String description;
    private String reference;
    private String channel;
    private Map<String, String> nvp = new HashMap<>();
    private String ipAddress;
    private String errorMessage;
}
