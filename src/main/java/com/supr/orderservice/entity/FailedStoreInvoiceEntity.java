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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Table(name = "failed_merchant_invoice", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"invoicePeriod", "storeId"})})
public class FailedStoreInvoiceEntity {

    @Id
    @GeneratedValue
    private long id;
    private String storeId;
    private String invoicePeriod;
    private String countryCode;
    @Enumerated(EnumType.STRING)
    private InvoiceCycle invoiceCycle;
    private long startTime;
    private long endTime;
}
