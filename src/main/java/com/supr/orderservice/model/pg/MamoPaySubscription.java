package com.supr.orderservice.model.pg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MamoPaySubscription implements Serializable {
    private String frequency;
    private int frequencyInterval;
    private String startDate;
    private String endDate;
    private int paymentQuantity;
}
