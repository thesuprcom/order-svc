package com.supr.orderservice.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchOrderRequest {
    @NotEmpty(message = "Seller Id cannot be empty")
    private String sellerId;
    @NotEmpty(message = "Brand Id cannot be empty")
    private String brandId;
    private String orderId;
    private String status;
    private String deliveryMethod;
}
