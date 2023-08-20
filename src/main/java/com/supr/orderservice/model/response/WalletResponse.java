package com.supr.orderservice.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class WalletResponse implements Serializable {
    private String walletId;
    private String userId;
    private String userEmail;
    private String walletStatus;
    private String totalAmount;
    private String amountAvailable;
    private String countryCode;
    private String currencyCode;
}
