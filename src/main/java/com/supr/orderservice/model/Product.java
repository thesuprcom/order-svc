package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Product {
    private String sku;
    private String brand;
    @JsonProperty("imageKey")
    private String imageKey;
    private String title;
    @JsonProperty("originalTitle")
    private String originalTitle;
    private String skuConfig;
    private String pskuCode;
    private String countryCode;
    private String sellerCode;
    private double msrp;
    private double shippingCharge;
    private double displayPrice;
    private Double offerPrice;
    private String warranty;
    private Integer stockCustomerLimit;
    private int stock;
    private boolean isInventoryTrack;
    private boolean isActive;
    private Misc misc;
}
