package com.supr.orderservice.model.notification;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmailAddress {
    private List<Email> to;
    private List<Email> cc;
}
