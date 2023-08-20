package com.supr.orderservice.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supr.orderservice.model.ImageUrl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FetchGreetingResponse implements Serializable {
    private String from;
    private String to;
    private ImageUrl greetingCardImage;
    private String senderName;
    private String greetingMessage;
    private ImageUrl greetingEnvelopUrl;
    private String orderId;
}
