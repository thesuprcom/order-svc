package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserDetails {

    private String name;

    private String mobileNo;

    private String emailId;

    private String userId;

    private String appTokenId;

    private String firstName;

    private String lastName;
    private String publicUrl;

    private String sellerId;
}
