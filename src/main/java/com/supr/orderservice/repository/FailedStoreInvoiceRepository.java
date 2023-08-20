package com.supr.orderservice.repository;

import com.supr.orderservice.entity.FailedStoreInvoiceEntity;
import com.supr.orderservice.enums.InvoiceCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FailedStoreInvoiceRepository extends JpaRepository<FailedStoreInvoiceEntity, Long> {

  List<FailedStoreInvoiceEntity> findByStoreIdAndInvoiceCycle(String storeId, InvoiceCycle invoiceCycle);

}
