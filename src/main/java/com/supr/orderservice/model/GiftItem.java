package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GiftItem implements Serializable {
    private String skus;
    private String psku;
    private String brandCode;
    private String brandName;
    private String sellerId;
    private BigDecimal salePrice;
    private String giftTitle;
    private BigDecimal quantity;
    private String giftCategory;
    private String giftDescription;
    private BigDecimal originalPrice;
    private BigDecimal discountPrice;
    private BigDecimal shippingPrice;
    private List<ImageUrl> giftImages;
    private String size;
    private String sizeUnit;
    private BigDecimal discountPercentage;
}
