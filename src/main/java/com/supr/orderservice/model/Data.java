package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@lombok.Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Data {
    public String publicId;
    public String firstName;
    public String lastName;
    public String emailId;
    public String phoneNumber;
    public int isActive;
    public String joinDate;
    public int isEnabled;
    public int isPhoneVerify;
    public int isEmailVerified;
    public String profileUrl;
}
