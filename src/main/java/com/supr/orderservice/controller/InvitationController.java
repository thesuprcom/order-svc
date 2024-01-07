package com.supr.orderservice.controller;

import com.supr.orderservice.service.InvitationService;
import com.supr.orderservice.service.ReceiverOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/gift-link/sender")
@RequiredArgsConstructor
public class InvitationController {
    private final InvitationService invitationService;
    private final ReceiverOrderService receiverOrderService;

    @GetMapping("{order-id}")
    ResponseEntity handleInvitationLink(@PathVariable("order-id") String orderId) {
        return ResponseEntity.ok(receiverOrderService.fetchGreeting(orderId));
    }

}
