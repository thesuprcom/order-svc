package com.supr.orderservice.controller;

import com.supr.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/internal")
@RequiredArgsConstructor
public class OrderScheduleController {
    private final OrderService orderService;

    @GetMapping("/process-scheduled-order")
    public ResponseEntity processScheduledOrder() {
        orderService.processScheduledOrder();
        return ResponseEntity.ok().build();
    }

}
