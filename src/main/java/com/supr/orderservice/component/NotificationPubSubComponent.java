package com.supr.orderservice.component;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.stereotype.Component;

@Component
@MessagingGateway(defaultRequestChannel = "notificationPubSubOutputChannel")
public interface NotificationPubSubComponent {
    void sendNotification(String request);
}
