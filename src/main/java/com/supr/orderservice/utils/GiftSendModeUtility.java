package com.supr.orderservice.utils;


import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.enums.NotificationEventEnum;
import com.supr.orderservice.enums.OrderChangeEvent;
import com.supr.orderservice.enums.StateChangeReason;
import com.supr.orderservice.enums.StateMachineType;
import com.supr.orderservice.repository.OrderRepository;
import com.supr.orderservice.service.InvitationLinkService;
import com.supr.orderservice.service.NotificationService;
import com.supr.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GiftSendModeUtility {
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final NotificationService notificationService;
    private final InvitationLinkService invitationLinkService;
    public void sendEmailForGift(OrderEntity order){
        String invitationLink = invitationLinkService.generateInvitationLink(order);
        order.setInvitationLink(invitationLink);
        orderRepository.save(order);
        orderService.changeOrderState(order, StateMachineType.SENDER.name(), OrderChangeEvent.SENDER_PAYMENT_SUCCESS.name(), true,
                StateChangeReason.PAYMENT_SUCCESS.getReason());
        notificationService.notify(order, NotificationEventEnum.GIFT_SEND_EMAIL, invitationLink);
    }
    public void sendGiftOnPhone(OrderEntity order){
        String invitationLink = invitationLinkService.generateInvitationLink(order);
        order.setInvitationLink(invitationLink);
        orderRepository.save(order);
        orderService.changeOrderState(order, StateMachineType.SENDER.name(), OrderChangeEvent.SENDER_PAYMENT_SUCCESS.name(), true,
                StateChangeReason.PAYMENT_SUCCESS.getReason());
        notificationService.notify(order, NotificationEventEnum.GIFT_SEND_SMS, invitationLink);
    }
    public void sendGiftOnInvitationLink(OrderEntity order){
        String invitationLink = invitationLinkService.generateInvitationLink(order);
        order.setInvitationLink(invitationLink);
        orderService.changeOrderState(order, StateMachineType.SENDER.name(), OrderChangeEvent.SENDER_SENT_GIFT_LINK.name(), true,
                StateChangeReason.PAYMENT_SUCCESS.getReason());
        orderRepository.save(order);
    }


    public void sendGiftDirectlyOnAddress(OrderEntity order) {
        orderService.changeOrderState(order, StateMachineType.SENDER.name(), OrderChangeEvent.SENDER_SENT_GIFT_DIRECTLY.name(), true,
                StateChangeReason.PAYMENT_SUCCESS.getReason());
    }
}
