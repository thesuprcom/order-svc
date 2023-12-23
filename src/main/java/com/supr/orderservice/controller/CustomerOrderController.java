package com.supr.orderservice.controller;

import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.enums.ExternalStatus;
import com.supr.orderservice.enums.OrderChangeEvent;
import com.supr.orderservice.enums.StateMachineType;
import com.supr.orderservice.exception.OrderServiceException;
import com.supr.orderservice.model.CustomerOrderDetail;
import com.supr.orderservice.model.response.CustomerOrderResponse;
import com.supr.orderservice.service.OrderService;
import com.supr.orderservice.service.StateMachineManager;
import com.supr.orderservice.service.WalletService;
import com.supr.orderservice.utils.ApplicationUtils;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/external/customer/order")
@RequiredArgsConstructor
public class CustomerOrderController {
    private final OrderService orderService;

    @GetMapping("/{order-id}")
    public ResponseEntity fetchOrder(@PathVariable(name = "order-id") String orderId) {
        OrderEntity orderEntity = orderService.fetchSenderOrder(orderId);
        CustomerOrderDetail customerOrderDetail = new CustomerOrderDetail();
        return ResponseEntity.ok(customerOrderDetail.customerOrderDetail(orderEntity));

    }

    @GetMapping("/list")
    public ResponseEntity fetchPastOrders(@RequestParam(value = "page", defaultValue = "0") int page,
                                          @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");
        Page<OrderEntity> orderEntityPage = orderService.fetchSenderOrderPastOrderList(pageable);
        CustomerOrderResponse customerOrderResponse = new CustomerOrderResponse();
        List<CustomerOrderDetail> customerOrderDetails = orderEntityPage.get().map(order -> {
            CustomerOrderDetail customerOrderDetail = new CustomerOrderDetail();
            return customerOrderDetail.customerOrderDetail(order);
        }).collect(Collectors.toList());
        customerOrderResponse.setCustomerOrderDetails(customerOrderDetails);
        customerOrderResponse.setTotalPages(orderEntityPage.getTotalPages());
        customerOrderResponse.setTotalOrders(orderEntityPage.getTotalElements());
        customerOrderResponse.setNumberOfOrders(orderEntityPage.getNumberOfElements());
        return ResponseEntity.ok(customerOrderResponse);

    }
}
