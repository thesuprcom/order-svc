package com.supr.orderservice.controller;

import com.supr.orderservice.model.request.CancelOrderRequest;
import com.supr.orderservice.model.request.PortalUpdateOrderRequest;
import com.supr.orderservice.model.request.SearchOrderRequest;
import com.supr.orderservice.model.request.StatusChangeRequest;
import com.supr.orderservice.service.SellerPortalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.supr.orderservice.utils.Constants.X_COUNTRY_CODE_HEADER_KEY;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/seller/order")
@RequiredArgsConstructor
public class SellerPortalController {
    private final SellerPortalService sellerPortalService;

    @GetMapping("/list/{seller-id}/{brand-code}/{order-status}")
    public ResponseEntity getOrderList(@RequestHeader(name = X_COUNTRY_CODE_HEADER_KEY) String countryCode,
                                       @PathVariable(name = "seller-id") String sellerId,
                                       @PathVariable(name = "brand-code") String brandCode,
                                       @PathVariable(name = "order-status") String orderStatus,
                                       @RequestParam(name = "days") int days,
                                       @RequestParam(value = "page", defaultValue = "0") int page,
                                       @RequestParam(value = "size", defaultValue = "20") int size) {
        return new ResponseEntity(sellerPortalService.getOrderList(countryCode, sellerId, brandCode, orderStatus, days,
                PageRequest.of(page, size, Sort.Direction.DESC, "id")), HttpStatus.OK);
    }

    @GetMapping("/{order-id}/{seller-id}/{brand-code}")
    public ResponseEntity getOrderDetail(@PathVariable(name = "order-id") String orderId,
                                         @PathVariable(name = "seller-id") String sellerId,
                                         @PathVariable(name = "brand-code") String brandCode) {
        return new ResponseEntity(sellerPortalService.getOrderDetail(orderId, sellerId, brandCode), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity updateOrder(@RequestBody @Valid PortalUpdateOrderRequest portalUpdateOrderRequest) {
        return new ResponseEntity(sellerPortalService.updateOrder(portalUpdateOrderRequest), HttpStatus.OK);
    }

    @PutMapping("/mark-shipped")
    public ResponseEntity markOrderShip(@RequestBody StatusChangeRequest request) {
        return new ResponseEntity(sellerPortalService.markOrderShipped(request), HttpStatus.OK);
    }

    @PutMapping("/cancel/{order-id")
    public ResponseEntity cancelOrder(@Valid @RequestBody CancelOrderRequest cancelOrderRequest) {
        sellerPortalService.cancelOrder(cancelOrderRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status-updates/{order-id}/{seller-id}/{brand-code}")
    public ResponseEntity fetchStatusUpdates(@PathVariable(name = "order-id") String orderId,
                                             @PathVariable(name = "seller-id") String sellerId,
                                             @PathVariable(name = "brand-code") String brandCode) {
        return new ResponseEntity(sellerPortalService.fetchStatusUpdates(orderId, sellerId, brandCode), HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity searchOrder(@RequestBody SearchOrderRequest searchOrderRequest) {
        return new ResponseEntity(sellerPortalService.searchOrder(searchOrderRequest), HttpStatus.OK);
    }

}
