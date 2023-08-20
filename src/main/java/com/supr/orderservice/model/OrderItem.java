package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.supr.orderservice.enums.OrderItemStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderItem {
    private String orderItemId;
    @JsonAlias({"product_title", "title"})
    private String productTitle;

    private String category;

    private BigDecimal originalPrice;

    private BigDecimal salePrice;

    private OrderItemStatus status;

    private String currency;

    private BigDecimal quantity;

    private List<ImageUrl> images;

    private OrderPrice itemPriceDetails;

    private BigDecimal vat;

    private BigDecimal commission;

    private String parentSku;

    private String sku;

    private String skuConfig;

    private String sellerSku;

    private String gtin;

    private String brand;

    private String size;

    private String sizeUnit;

    private BigDecimal bufferAmountInPercentage;

    private int minAgeRequired;

    private String weighableType;
}
