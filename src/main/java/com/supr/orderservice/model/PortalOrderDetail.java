package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.utils.OrderUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;

import static com.supr.orderservice.utils.OrderUtils.fetchItemInfos;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PortalOrderDetail {
    private String orderId;
    private String  sellerId;
    private String brandId;
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

    public PortalOrderDetail(OrderEntity order){
        this.orderId = order.getOrderId();
        this.sellerId = order.getSellerId();
        this.countryCode = order.getCountryCode();
        this.currencyCode = order.getCurrencyCode();
        this.orderDate = order.getCreatedAt();
        this.orderTime = order.getOrderPlacedTime().toString();
        this.totalAmount = order.getTotalAmount().toString();
        this.itemInfos = fetchItemInfos(order.getOrderItemEntities());
        this.orderPrice = order.getPrice();
        this.customer = order.getSender().getFirstName() +" "+ order.getSender().getLastName();
        this.customerInfo = order.getSender();
        this.shippingAddress = order.getShippingAddress();
        this.billingAddress = order.getBillingAddress();
        this.status = order.getExternalStatus().name();
        this.time = OrderUtils.fetchTime(order.updatedAt);
        this.deliveryMethod = order.getDeliveryType();
        this.itemCount = order.getOrderItemEntities().size();
    }
}
