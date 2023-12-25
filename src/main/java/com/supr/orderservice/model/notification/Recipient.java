package com.supr.orderservice.model.notification;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Recipient {
    private List<PhoneNumber> phoneNumbers;
    private EmailAddress emailAddresses;
    private String appTokenId;
}
