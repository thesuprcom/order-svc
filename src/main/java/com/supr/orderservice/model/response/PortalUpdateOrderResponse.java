package com.supr.orderservice.model.response;

import com.supr.orderservice.model.PortalOrderDetail;
import lombok.Data;

@Data
public class PortalUpdateOrderResponse {
    private PortalOrderDetail portalOrderDetail;
}
