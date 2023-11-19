package com.supr.orderservice.service;

import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.entity.OrderItemEntity;
import com.supr.orderservice.model.request.UpdateQuantityRequest;
import com.supr.orderservice.model.response.UpdateQuantityResponse;

public interface OrderInventoryManagementService {

    UpdateQuantityResponse updateStoreOrderQuantity(OrderEntity order);

}
