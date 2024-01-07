package com.supr.orderservice.service;

import com.supr.orderservice.model.request.CancelOrderRequest;
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
    PortalOrderDetailListResponse getOrderList(String countryCode, String sellerId, String brandCode,
                                               String orderStatus , int days, Pageable pageable);

    PortalOrderDetailResponse getOrderDetail(String orderId, String sellerId, String brandCode);

    PortalUpdateOrderResponse updateOrder(PortalUpdateOrderRequest portalUpdateOrderRequest);

    PortalOrderDetailResponse markOrderShipped(StatusChangeRequest request);

    PortalOrderStatusUpdatesResponse fetchStatusUpdates(String orderId, String sellerId, String brandCode);

    PortalOrderSearchResponse searchOrder(SearchOrderRequest searchOrderRequest);

    void cancelOrder(CancelOrderRequest cancelOrderRequest);
}
