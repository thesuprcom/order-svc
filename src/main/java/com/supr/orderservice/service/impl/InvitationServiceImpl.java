package com.supr.orderservice.service.impl;

import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.enums.OrderType;
import com.supr.orderservice.model.GreetingCard;
import com.supr.orderservice.model.response.FetchGreetingResponse;
import com.supr.orderservice.repository.SenderOrderRepository;
import com.supr.orderservice.service.InvitationService;
import com.supr.orderservice.service.ReceiverOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {
    private final ReceiverOrderService receiverOrderService;

    @Override
    public FetchGreetingResponse handleInvitation(String orderId) {
        return receiverOrderService.fetchGreeting(orderId);
    }
}
