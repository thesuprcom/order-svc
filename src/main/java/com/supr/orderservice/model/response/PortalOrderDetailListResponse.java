package com.supr.orderservice.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.supr.orderservice.model.OrderCount;
import com.supr.orderservice.model.PortalOrderDetail;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PortalOrderDetailListResponse {
    private List<PortalOrderDetail> orderDetails;
    private int noOfDays;
    private List<OrderCount> orderCount;
    private long totalOrderCount;
    private long totalReturnOrderCount;
    private long totalFulfilledOrderCount;
    private long totalOrderItems;
}

