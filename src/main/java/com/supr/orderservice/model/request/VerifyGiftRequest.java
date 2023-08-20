package com.supr.orderservice.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class VerifyGiftRequest {
    private String orderId;
    private String emailId;
    private String phoneNumber;
    @NotEmpty(message = "Country code cannot be null")
    private String countryCode;
}
