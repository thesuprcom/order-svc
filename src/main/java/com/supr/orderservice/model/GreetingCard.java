package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GreetingCard {
    private String greetingCardCode;
    private String receiverName;
    private String greetingCardName;
    private String greetingCardOccasion;
    private String greetingCardImageUrl;
    private String greetingCardMsg;
    private ImageUrl greetingCardImage;
}
