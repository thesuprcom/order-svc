package com.supr.orderservice.service.impl;

import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.service.InvoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class InvoiceServiceImpl implements InvoiceService {
    @Override
    public void generateReceiverInvoice(OrderEntity order) {

    }

    @Override
    public void generateSenderInvoice(OrderEntity order) {

    }

    @Override
    public void generateMerchantInvoice(List<OrderEntity> orders) {

    }
}
