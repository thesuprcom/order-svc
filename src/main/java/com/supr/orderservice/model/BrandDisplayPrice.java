package com.supr.orderservice.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BrandDisplayPrice implements Serializable {
    private String brandName;
    private String totalDisplayPrice;
    private BigDecimal totalPrice;
    private List<GiftItemPrice> giftItemPrices;
    private String totalDisplayShippingCharge;
    private BigDecimal totalShippingCharges;

}
