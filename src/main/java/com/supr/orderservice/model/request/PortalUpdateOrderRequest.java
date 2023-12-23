package com.supr.orderservice.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.supr.orderservice.model.Address;
import com.supr.orderservice.model.ItemChangeDto;
import com.supr.orderservice.model.TrackingInfo;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PortalUpdateOrderRequest {
    @NotEmpty(message = "Order Id cannot be empty")
    private String orderId;
    @NotEmpty(message = "Seller Id cannot be empty")
    private String sellerId;
    @NotEmpty(message = "Brand code cannot be empty")
    private String brandCode;
    private Address shippingAddress;
    private String notes;
    private String contactEmail;
    private String contactPhone;
    @NotEmpty(message = "Updated By cannot be empty")
    private String updatedBy;
}
