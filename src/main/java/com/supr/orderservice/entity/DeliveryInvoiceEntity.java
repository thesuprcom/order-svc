package com.supr.orderservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "delivery_invoice_data")
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class DeliveryInvoiceEntity extends BaseEntity {
    private String countryCode;
    private String currency;
    private String description;
    private String filePath;
    private String invoiceNumber;
    private BigDecimal priceExcludingVat;
    private BigDecimal priceIncludingVat;
    private int quantity;
    private BigDecimal vatAmount;
    private String noonTrn;
    private String orderId;

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
