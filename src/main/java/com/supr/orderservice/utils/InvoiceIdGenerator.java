package com.supr.orderservice.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
public class InvoiceIdGenerator {

    private DateFormat dateFormat;
    private Date date = new Date();

    public String generateInvoiceId(final Long invoiceId) {
        return Constants.DELIVERY_INVOICE_PREFIX + getYear() + getMonth() + getRowCountInDesiredFormat(invoiceId);
    }

    @SneakyThrows
    private String getRowCountInDesiredFormat(final Long invoiceId) {
        return Constants.NUMBER_OF_DIGITS_FOR_DELIVERY_FEES_INVOICE
                .substring(0, Constants.NUMBER_OF_DIGITS_FOR_DELIVERY_FEES_INVOICE.length() - String.valueOf(invoiceId)
                        .length()) + invoiceId;
    }

    public String getYear() {
        dateFormat = new SimpleDateFormat(Constants.YEAR_IN_TWO_DIGITS);
        return dateFormat.format(date);
    }

    private String getMonth() {
        dateFormat = new SimpleDateFormat(Constants.MONTH_IN_TWO_DIGITS);
        return dateFormat.format(date);
    }

}
