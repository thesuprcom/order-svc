package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Misc {
    private BigDecimal vatRate;
    private BigDecimal costPrice;
    private BigDecimal salePrice;
    private String partnerSku;
    private int pskuIsActive;
    private int stockIsActive;
    private BigDecimal costPriceExVat;
    private String idProductParent;
    private BigDecimal salePriceExVat;
    private BigDecimal shippingVatRate;
    private BigDecimal shippingChargeExVat;
}
