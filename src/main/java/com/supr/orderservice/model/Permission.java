package com.supr.orderservice.model;

import lombok.Data;

@Data
public class Permission {
    private long id;

    private String name;

    private String serviceId;

    private String uid;

}
