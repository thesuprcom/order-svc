package com.supr.orderservice.model.response;

import com.supr.orderservice.model.PortalOrderDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortalOrderSearchResponse {
    private List<PortalOrderDetail> orderDetails;
}
