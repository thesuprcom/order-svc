package com.supr.orderservice.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class VerifyGiftOtpRequest {
    @NotEmpty(message = "OTP Code cannot be empty")
    private long otpCode;
    private String phoneNumber;
    private String emailId;
    @NotEmpty(message = "Public Id cannot be empty")
    private String publicId;
    @NotEmpty(message = "Coutry Code cannot be empty")
    private String countryCode;
    @NotEmpty(message = "Verify Token cannot be empty")
    private String verifyToken;
    @NotEmpty(message = "Order id cannot be empty")
    private String orderId;
    @NotEmpty(message = "Client Reference cannot be empty")
    private String clientReference;
}
