package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SellerInfo {
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
