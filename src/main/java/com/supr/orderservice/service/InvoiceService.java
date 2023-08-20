package com.supr.orderservice.service;

import com.supr.orderservice.entity.OrderEntity;

import java.util.List;

public interface InvoiceService {
    void generateReceiverInvoice(OrderEntity order);
    void generateSenderInvoice(OrderEntity order);
    void generateMerchantInvoice(List<OrderEntity> orders);
}
