package com.supr.orderservice.model.request;

import com.supr.orderservice.model.ItemStatusChange;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class StatusChangeRequest {
    @NotEmpty(message = "Order Id cannot be empty")
    private String orderId;
    @NotEmpty(message = "Seller Id cannot be empty")
    private String sellerId;
    @NotEmpty(message = "Brand code cannot be empty")
    private String brandCode;
    private List<ItemStatusChange> itemStatusChanges;
    private String orderStatus;
}
