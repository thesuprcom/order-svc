package com.supr.orderservice.model.notification;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
@Builder
@ToString
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class NotificationRequest {
    private String sender;
    private String event;
    private Recipient recipient;
    private List<TemplateDynamicValue> dynamicMessageInputs;
    /**
     * <p>Represents the 'data' part of FCM Message which accepts arbitrary key/value payload
     * refer: https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages<br><br>
     * The name has been kep 'data' deliberately to match the key name in FCM Message
     * </p>
     */
    private Map<String, String> data;
}
