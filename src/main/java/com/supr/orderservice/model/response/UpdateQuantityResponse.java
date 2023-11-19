package com.supr.orderservice.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UpdateQuantityResponse {
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class DataItem {
        public String actionType;
        public String countryCode;
        public String error;
        public int idPskuPriceWithStock;
        public boolean invalid;
        public String pskuCode;
        public int qty;
        public String stock;
        public String updatedBy;
    }

    public List<DataItem> data;
    public String message;
}