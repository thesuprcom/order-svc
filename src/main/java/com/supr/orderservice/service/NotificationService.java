package com.supr.orderservice.service;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.supr.orderservice.component.NotificationPubSubComponent;
import com.supr.orderservice.entity.OrderEntity;
import com.supr.orderservice.entity.OrderItemEntity;
import com.supr.orderservice.enums.NotificationEventEnum;
import com.supr.orderservice.model.notification.Email;
import com.supr.orderservice.model.notification.EmailAddress;
import com.supr.orderservice.model.notification.NotificationRequest;
import com.supr.orderservice.model.notification.PhoneNumber;
import com.supr.orderservice.model.notification.Recipient;
import com.supr.orderservice.model.notification.TemplateDynamicValue;
import com.supr.orderservice.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final Gson gson;
    private final NotificationPubSubComponent gcpPubSubComponent;

    @Async
    public void notify(OrderEntity order, NotificationEventEnum notificationEvent, String invitationLink) {
        try {
            List<PhoneNumber> phoneNumbers = new ArrayList<>();
            EmailAddress emailAddress = new EmailAddress();
            switch (notificationEvent) {
                case GIFT_SEND_SMS -> phoneNumbers = getPhoneNumbers(order.getReceiverPhone(), order.getCountryCode());
                case GIFT_SEND_EMAIL -> emailAddress = getEmailAddress(order);

            }

            Recipient recipient = Recipient.builder()
                    .phoneNumbers(phoneNumbers)
                    .emailAddresses(emailAddress)
                    .appTokenId(null)
                    .build();

            List<TemplateDynamicValue> templateDynamicValues = createTemplateDynamicValues(order, invitationLink);

            final NotificationRequest.NotificationRequestBuilder notificationRequestBuilder =
                    NotificationRequest.builder()
                            .sender(Constants.NOTIFICATION_SENDER_NAME)
                            .event(notificationEvent.name())
                            .recipient(recipient)
                            .dynamicMessageInputs(templateDynamicValues);

            NotificationRequest notificationRequest = notificationRequestBuilder.build();
            log.info("Request Payload for sending notification for User : {} on Event : {}", order.getReceiverEmail(),
                    notificationEvent.name());
            log.info("Sending notification: {}", gson.toJson(notificationRequest));

            gcpPubSubComponent.sendNotification(gson.toJson(notificationRequest));
            log.info("Notification request sent for User : {} on Event : {}", order.getReceiverEmail(),
                    notificationEvent.name());

        } catch (Exception exception) {
            log.error("Failed to send notification for User: {} and Event : {}. Exception occurred: {}",
                    order.getReceiverEmail(), notificationEvent.name(), exception);
        }
    }

    private EmailAddress getEmailAddress(OrderEntity order) {
        EmailAddress emailAddress = new EmailAddress();
        emailAddress.setTo(Lists.newArrayList(
                Email.builder()
                        .email(order.getReceiverEmail())
                        .name(order.getReceiver().getFirstName())
                        .build()));

        return emailAddress;
    }


    private List<PhoneNumber> getPhoneNumbers(String phoneNumber, String countryCode) {
        List<PhoneNumber> phoneNumbers;
        String phoneCountryCode = findCountryCode(countryCode);
        if (phoneNumber.trim().charAt(0) == '+') {
            String[] pairs = phoneNumber.split("\\Q" + phoneCountryCode + "\\E");
            phoneNumbers =
                    Lists.newArrayList(PhoneNumber.builder().countryCode(phoneCountryCode).number(pairs[1]).build());
        } else {
            phoneNumbers =
                    Lists.newArrayList(PhoneNumber.builder().countryCode(phoneCountryCode).number(phoneNumber.trim()).build());
        }
        return phoneNumbers;
    }

    private String findCountryCode(String countryCode) {
        if (countryCode.trim().equalsIgnoreCase("ae"))
            return "+971";
        else if (countryCode.trim().equalsIgnoreCase("in"))
            return "+91";
        else return "+1";
    }

    private List<TemplateDynamicValue> createTemplateDynamicValues(OrderEntity order, String invitationLink) {

        TemplateDynamicValue userNameTemplateValue = TemplateDynamicValue.builder()
                .variableName(Constants.USER_NAME_VARIABLE_NAME)
                .variableValue(order.getReceiver().getFirstName() + " " + order.getReceiver().getLastName())
                .build();

        TemplateDynamicValue invitationLinkTemplateValue = TemplateDynamicValue.builder()
                .variableName(Constants.INVITATION_LINK_VARIABLE_NAME)
                .variableValue(invitationLink)
                .build();


        return Lists.newArrayList(
                userNameTemplateValue,
                invitationLinkTemplateValue);
    }

}
