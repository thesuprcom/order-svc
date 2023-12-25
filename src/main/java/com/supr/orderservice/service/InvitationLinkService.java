package com.supr.orderservice.service;

import com.supr.orderservice.entity.OrderEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InvitationLinkService {
    @Value("${invitation-link.base-url}")
    private String invitationLinkUrl;

    public String generateInvitationLink(OrderEntity orderEntity) {
        return invitationLinkUrl + "/" + orderEntity.getOrderId();
    }
}
