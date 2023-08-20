package com.supr.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Address {

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