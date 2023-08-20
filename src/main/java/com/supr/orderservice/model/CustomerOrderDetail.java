package com.supr.orderservice.model;

import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.utils.OrderUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class CustomerOrderDetail implements Serializable {
    private List<ItemInfo> items;
    private String orderId;
    private OrderPrice orderPrice;
    private String status;
    private String orderPlacedDate;
    private String receiverName;
    private GreetingCard greetingCard;
    private String invoiceLink;
    private BigDecimal totalAmount;
    private Address shippingAddress;
    private SellerInfo sellerInfo;
    private CouponDetails couponDetails;

    public CustomerOrderDetail customerOrderDetail(OrderEntity order){
        CustomerOrderDetail response = new CustomerOrderDetail();
        response.setItems(OrderUtils.fetchItemInfos(order.getOrderItemEntities()));
        response.setOrderId(order.getOrderId());
        response.setOrderPrice(order.getPrice());
        response.setStatus(order.getExternalStatus().name());
        response.setOrderPlacedDate(order.createdAt.toString());
        response.setReceiverName(order.getReceiver().getName());
        response.setGreetingCard(OrderUtils.fetchGreetingCard(order.getGreetingCard()));
        response.setInvoiceLink(order.getInvoiceDocumentId());
        response.setShippingAddress(order.getShippingAddress());
        response.setSellerInfo(order.getSellerInfo());
        response.setCouponDetails(order.getCouponDetails());
        return response;
    }
}
