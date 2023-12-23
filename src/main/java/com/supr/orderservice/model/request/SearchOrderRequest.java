package com.supr.orderservice.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchOrderRequest {
    @NotEmpty(message = "Order Id cannot be empty")
    private String orderId;
    @NotEmpty(message = "Seller Id cannot be empty")
    private String sellerId;
    @NotEmpty(message = "Brand code cannot be empty")
    private String brandCode;
    private String status;
    private String deliveryMethod;
}
