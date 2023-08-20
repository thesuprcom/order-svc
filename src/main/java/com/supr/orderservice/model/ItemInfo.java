package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supr.orderservice.entity.OrderItemStatusHistoryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemInfo {

    private String pskuCode;

    private String productId;

    private String parentProductId;

    private String warranty;

    private String partnerSku;

    private String productTitle;

    private String productColour;

    private String productFullTitle;

    private String productBrand;

    private String productFamily;

    private String productSubtype;

    private String productType;

    private List<ImageUrl> images;
    private ImageUrl itemImage;

    private BigDecimal quantity;

    private BigDecimal totalAmount;

    private String sellerId;

    private String parentSku;

    private OrderPrice itemPriceDetails;
    private TrackingInfo trackingInfo;
    private String itemStatus;
    private List<OrderItemStatusHistoryEntity> orderItemStatusHistory;

}
