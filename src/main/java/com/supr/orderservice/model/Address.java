package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Address implements Serializable {

    private Integer id;
    private Boolean deleted;
    @JsonIgnore
    private Integer userId;
    private String customerName;
    private String addressLine;
    private String area;
    private String landmark;
    private String city;
    private String country;
    private String addressType;
    private String addressName;
    private Map<String, Object> addressMetadata = new HashMap<>();

}