package com.supr.orderservice.utils;

import com.supr.orderservice.entity.GreetingCardEntity;
import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.enums.ExternalStatus;
import com.supr.orderservice.enums.OrderItemStatus;
import com.supr.orderservice.enums.OrderType;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ReceiverOrderUtil {
    public static OrderEntity createReceiverOrder(OrderEntity senderOrder, GreetingCardEntity greetingCard){
        OrderEntity order = new OrderEntity();
        order.setOrderId(senderOrder.getOrderId());
        order.setOrderType(OrderType.RECEIVER);
        order.setBrandId(senderOrder.getBrandId());
        order.setInvitationLink(senderOrder.getInvitationLink());
        order.setReceiverEmail(senderOrder.getReceiverEmail());
        order.setReceiverPhone(senderOrder.getReceiverPhone());
        order.setReceiver(senderOrder.getReceiver());
        order.setCountryCode(senderOrder.getCountryCode());
        order.setCurrencyCode(senderOrder.getCurrencyCode());
        order.setGiftSentOption(senderOrder.getGiftSentOption());
        order.setSellerId(senderOrder.getSellerId());
        order.setSellerInfo(senderOrder.getSellerInfo());
        order.setSender(senderOrder.getSender());
        order.setOrderPlacedTime(DateUtils.getCurrentDateTimeUTC());
        order.setOrderItemEntities(senderOrder.getOrderItemEntities());
        order.setGreetingCard(greetingCard);
        order.setReceiver(senderOrder.getReceiver());
        return order;
    }
}
