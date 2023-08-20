package com.supr.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreInvoiceDetails implements Serializable {
    private String invoiceUrl;
    private String invoiceNumber;
}