package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ItemPrice {
    public BigDecimal vatRate;
    public BigDecimal costPrice;
    public BigDecimal salePrice;
    public String partnerSku;
    public boolean pskuIsActive;
    public boolean stockIsActive;
    public BigDecimal costPriceExVat;
    public String idProductParent;
    public BigDecimal salePriceExVat;
    public BigDecimal shippingVatRate;
    public BigDecimal shippingChargeExVat;
}
