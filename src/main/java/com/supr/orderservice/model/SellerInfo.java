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
    private String brandId;
    private String storeId;
    private String storeAccountNumber;
    private String city;
    private String status;
    private String country;
    private String brandName;
    private String storeName;
    private String companyLegalName;
    private String companyPhoneNo;
    private String sellerType;
    private String sellerAddress;
    private String sellerCurrency;
    private String taxRegistrationNumber;
    private String area;
    private List<String> images;
    private String countryCode;
    private String estimatedDeliveryTime;
}
