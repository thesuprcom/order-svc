package com.supr.orderservice.service;

import com.supr.orderservice.entity.OrderEntity;

public interface SenderOrderService {

  OrderEntity fetchOrder(String orderId);

}
