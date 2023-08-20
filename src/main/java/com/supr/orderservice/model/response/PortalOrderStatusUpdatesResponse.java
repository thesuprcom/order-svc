package com.supr.orderservice.model.response;

import com.supr.orderservice.entity.OrderItemStatusHistoryEntity;
import com.supr.orderservice.model.PortalOrderDetail;
import lombok.Data;

import java.util.List;

@Data
public class PortalOrderStatusUpdatesResponse {
    private List<OrderItemStatusHistoryEntity> orderItemStatus;
}
