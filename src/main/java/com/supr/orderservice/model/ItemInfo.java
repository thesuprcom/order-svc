package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ItemInfo implements Serializable {

    private String skus;
    private String psku;
    private String brandCode;
    private String brandName;
    private String sellerId;
    private BigDecimal salePrice;
    private String giftTitle;
    private SellerInfo sellerInfo;
    @NotNull
    private BigDecimal quantity;
    private BigDecimal orderItemQuantityShipped;
    private BigDecimal orderItemQuantityCancelled;
    private BigDecimal orderItemQuantityRemaining;
    private String giftCategory;
    private String giftDescription;
    private BigDecimal originalPrice;
    private BigDecimal discountPrice;
    private BigDecimal shippingPrice;
    private String orderItemId;
    private String childOrderId;
    private List<ImageUrl> giftImages;
    private String size;
    private String sizeUnit;
    private BigDecimal discountPercentage;
    private OrderPrice itemPriceDetails;
    private TrackingInfo trackingInfo;
    private String itemStatus;



    private List<OrderItemStatusHistory> orderItemStatusHistory;

}
