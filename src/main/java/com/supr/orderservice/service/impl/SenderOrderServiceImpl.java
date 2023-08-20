package com.supr.orderservice.service.impl;

import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.enums.OrderType;
import com.supr.orderservice.repository.SenderOrderRepository;
import com.supr.orderservice.service.SenderOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SenderOrderServiceImpl implements SenderOrderService {
    private final SenderOrderRepository senderOrderRepository;

    @Override
    public OrderEntity fetchOrder(String orderId) {
        return senderOrderRepository.findByOrderIdAndOrderType(orderId, OrderType.SENDER);
    }


}
