package com.supr.orderservice.model.response;

import com.supr.orderservice.model.OrderCount;
import com.supr.orderservice.model.PortalOrderDetail;
import lombok.Data;

import java.util.List;

@Data
public class PortalOrderDetailListResponse {
    private List<PortalOrderDetail> orderDetails;
    private int noOfDays;
    private List<OrderCount> orderCount;
    private long totalOrderCount;
    private long totalOrderItems;
}

