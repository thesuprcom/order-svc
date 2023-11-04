package com.supr.orderservice.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DisplayPriceDetails implements Serializable {
    private String key;
    private String value;

    public DisplayPriceDetails(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
