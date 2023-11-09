package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SellerInfo implements Serializable {
    private String sellerId;
    private String city;
    private String countryName;
    private String sellerAccountNumber;
    private String status;
    private String sellerName;
    private String countryCode;
    private String currencyCode;
    private String sellerAddress;
    private String sellerLegalName;
    private String sellerPhoneNo;

}
