package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserInfo implements Serializable {
    private String userId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String publicId;
    private String emailId;
    private int isActive;
    private int isEnabled;
    private int isPhoneVerify;
    private int isEmailVerified;
    private String profileUrl;
    @JsonIgnore
    private String token;
    @JsonIgnore
    private boolean isNewUser;
    private Address address;
}
