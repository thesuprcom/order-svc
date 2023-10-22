package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ViewGiftItem {
    private List<ImageUrl> itemImage;
    private String description;
    private String pskuCode;
    private String productId;
    private String warranty;
    private String productTitle;
    private String productFullTitle;
    private String productBrand;
    private BigDecimal quantity;
}
