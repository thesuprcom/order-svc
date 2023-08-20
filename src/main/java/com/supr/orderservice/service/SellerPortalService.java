package com.supr.orderservice.service;

import com.supr.orderservice.model.request.PortalUpdateOrderRequest;
import com.supr.orderservice.model.request.SearchOrderRequest;
import com.supr.orderservice.model.request.StatusChangeRequest;
import com.supr.orderservice.model.response.PortalOrderDetailListResponse;
import com.supr.orderservice.model.response.PortalOrderDetailResponse;
import com.supr.orderservice.model.response.PortalOrderSearchResponse;
import com.supr.orderservice.model.response.PortalOrderStatusUpdatesResponse;
import com.supr.orderservice.model.response.PortalUpdateOrderResponse;
import org.springframework.data.domain.Pageable;

public interface SellerPortalService {
    PortalOrderDetailListResponse getOrderList(String countryCode, String sellerId, String brandId, int days,
                                               Pageable pageable);
    PortalOrderDetailResponse getOrderDetail(String orderId);
    PortalUpdateOrderResponse updateOrder(PortalUpdateOrderRequest portalUpdateOrderRequest);
    PortalOrderDetailResponse markOrderShip(StatusChangeRequest request);
    PortalOrderStatusUpdatesResponse fetchStatusUpdates(String orderId);

    PortalOrderSearchResponse searchOrder(SearchOrderRequest searchOrderRequest);
}
