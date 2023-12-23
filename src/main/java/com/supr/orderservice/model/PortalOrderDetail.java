package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.entity.OrderItemEntity;
import com.supr.orderservice.utils.OrderUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;

import static com.supr.orderservice.utils.OrderUtils.fetchItemInfos;
import static com.supr.orderservice.utils.OrderUtils.fetchTotalAmount;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PortalOrderDetail {
    private String orderId;
    private String sellerId;
    private String brandCode;
    private String countryCode;
    private String currencyCode;
    private Date orderDate;
    private String orderTime;
    private String totalAmount;
    private List<ItemInfo> itemInfos;
    private OrderPrice orderPrice;
    private String customer;
    private UserInfo customerInfo;
    private Address shippingAddress;
    private Address billingAddress;
    private String status;
    private String time;
    private String deliveryMethod;
    private long itemCount;

    public PortalOrderDetail(OrderEntity order, List<OrderItemEntity> orderItemEntities, String sellerId,
                             String brandCode) {
        this.orderId = order.getOrderId();
        this.sellerId = sellerId;
        this.brandCode = brandCode;
        this.countryCode = order.getCountryCode();
        this.currencyCode = order.getCurrencyCode();
        this.orderDate = order.getCreatedAt();
        this.orderTime = order.getOrderPlacedTime().toString();
        this.totalAmount = fetchTotalAmount(orderItemEntities);
        this.itemInfos = fetchItemInfos(orderItemEntities);
        this.customer = order.getSender().getFirstName() + " " + order.getSender().getLastName();
        this.customerInfo = order.getSender();
        this.shippingAddress = order.getShippingAddress();
        this.billingAddress = order.getBillingAddress();
        this.status = order.getExternalStatus().name();
        this.time = OrderUtils.fetchTime(order.getUpdatedAt());
        this.deliveryMethod = order.getDeliveryType();
        this.itemCount = order.getOrderItemEntities().size();
    }
}
