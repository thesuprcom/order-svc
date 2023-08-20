package com.supr.orderservice.model.request;

import com.supr.orderservice.model.ItemStatusChange;
import lombok.Data;

import java.util.List;

@Data
public class StatusChangeRequest {
    private String orderId;
    private List<ItemStatusChange> itemStatusChanges;
    private String orderStatus;
}
