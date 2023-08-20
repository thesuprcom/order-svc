package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Offer {
    public String sku;
    public String skuConfig;
    public String url;
    public String pskuCode;
    public String sellerCode;
    public BigDecimal msrp;
    public BigDecimal shippingCharge;
    public BigDecimal displayPrice;
    public String warranty;
    public Integer stockCustomerLimit;
    public int stock;
    public boolean isInventoryTrack;
    public ItemPrice misc;
}
