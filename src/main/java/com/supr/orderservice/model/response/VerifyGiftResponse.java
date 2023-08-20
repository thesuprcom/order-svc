package com.supr.orderservice.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.supr.orderservice.model.EmailVerification;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VerifyGiftResponse {
    private String phoneNumber;
    private String emailId;
    private String orderId;
    private String from;
    private String to;
    private com.supr.orderservice.model.Data data;
    public EmailVerification emailVerification;
    public boolean success;
    private String clientReference;
}
