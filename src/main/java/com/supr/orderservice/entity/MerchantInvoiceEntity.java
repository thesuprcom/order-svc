package com.supr.orderservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.supr.orderservice.enums.InvoiceCycle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Table(name = "merchant_invoice", uniqueConstraints = {@UniqueConstraint(columnNames = {"invoicePeriod", "storeId"})})

public class MerchantInvoiceEntity extends BaseEntity{

    private String invoiceNumber;
    private String storeId;
    private String productCode;
    private String description;
    private int quantity;
    private BigDecimal priceExcludingVat;
    private BigDecimal vatAmount;
    private BigDecimal priceIncludingVat;
    private String filePath;
    private String currency;
    private LocalDate invoicePeriodFromDate;
    private LocalDate invoicePeriodToDate;
    private String invoicePeriod;
    private String countryCode;
    @Enumerated(EnumType.STRING)
    private InvoiceCycle invoiceCycle;
    private BigDecimal orderNetValue;
    private Integer totalItems;

}
