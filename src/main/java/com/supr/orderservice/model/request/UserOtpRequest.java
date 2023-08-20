package com.supr.orderservice.model.request;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserOtpRequest {
    private String emailId;
    private String publicId;
    private String countryCode;
    private String phoneNumber;
    private String clientReference;
}
