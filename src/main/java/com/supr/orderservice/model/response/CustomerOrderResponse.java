package com.supr.orderservice.model.response;

import com.supr.orderservice.model.CustomerOrderDetail;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class CustomerOrderResponse {
    private List<CustomerOrderDetail> customerOrderDetails;
    private String status;
    private int noOfOrders;
    private int totalPages;
    private long totalOrders;
    private int numberOfOrders;

}
