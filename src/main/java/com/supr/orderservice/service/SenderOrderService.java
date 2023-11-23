package com.supr.orderservice.service;

import com.supr.orderservice.entity.OrderEntity;
import org.hibernate.criterion.Order;

public interface SenderOrderService {

  OrderEntity fetchOrder(String orderId);

  OrderEntity fetchOrderFromCartIdentifier(String cartIdentifier);

}
