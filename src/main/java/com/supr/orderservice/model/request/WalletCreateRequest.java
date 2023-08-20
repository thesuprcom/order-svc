package com.supr.orderservice.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class WalletCreateRequest implements Serializable {


    @NotEmpty(message = "User id cannot be empty")
    private String userId;

    @NotEmpty(message = "User Email cannot be null")
    private String userEmail;

    private String orderId;
    @NotEmpty(message = "Amount cannot be null")
    private String totalAmount;

}
