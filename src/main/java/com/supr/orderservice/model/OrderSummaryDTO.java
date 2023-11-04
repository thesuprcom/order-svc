package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OrderSummaryDTO implements Serializable {
    private List<BrandDisplayPrice> brandDisplayPrices;
    private List<DisplayPriceDetails> displayPriceDetails;
    @JsonIgnore
    private BigDecimal totalAmount;
    @JsonIgnore
    private BigDecimal totalShipping;
}
