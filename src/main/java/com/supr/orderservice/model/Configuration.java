package com.supr.orderservice.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Configuration {
    private Boolean tokenizeCc;
    private String returnUrl;
    private String locale;
}
