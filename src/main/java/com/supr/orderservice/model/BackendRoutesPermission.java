package com.supr.orderservice.model;

import lombok.Data;

@Data
public class BackendRoutesPermission {

    private String route;
    private String method;
    private String permissions;
    private int priority;
}

