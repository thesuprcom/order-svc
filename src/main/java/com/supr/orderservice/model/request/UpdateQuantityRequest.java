package com.supr.orderservice.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateQuantityRequest {

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class DataItem {
    public String pskuCode;
    public String stock;
    public String sellerId;
    public String actionType;
  }

  public List<DataItem> data;
  public String countryCode;
  public String updatedBy;
}
