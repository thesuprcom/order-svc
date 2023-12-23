package com.supr.orderservice.model.request;

import com.supr.orderservice.model.ItemStatusChange;
import com.supr.orderservice.model.TrackingInfo;
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
    private List<ItemStatusChange> itemUpdates;
    private boolean isOrderLevelTracking;
    private TrackingInfo orderTrackingInfo;
    @NotEmpty(message = "Updated By cannot be empty")
    private String updatedBy;
}
