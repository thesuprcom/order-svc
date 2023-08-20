package com.supr.orderservice.repository;

import com.supr.orderservice.entity.MerchantInvoiceEntity;
import com.supr.orderservice.enums.InvoiceCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MerchantInvoiceRepository extends JpaRepository<MerchantInvoiceEntity, Long> {

  List<MerchantInvoiceEntity> findByStoreIdAndInvoiceCycleAndCountryCodeOrderByCreatedAtDesc(String storeId,
                                                                                       InvoiceCycle invoiceCycle,
                                                                                       String countryCode);

  MerchantInvoiceEntity findByStoreIdAndInvoiceCycleAndInvoicePeriod(String storeId, InvoiceCycle invoiceCycle,
                                                               String invoicePeriod);

  MerchantInvoiceEntity findByStoreIdAndCountryCodeAndInvoicePeriod(String storeId, String countryCode,
                                                              String invoicePeriod);

  List<MerchantInvoiceEntity> findByStoreIdInAndInvoicePeriodToDateLessThanEqual(List<String> storeId, LocalDate toDate);

  List<MerchantInvoiceEntity> findByStoreIdAndAndInvoicePeriodToDateLessThanEqual(String storeId, LocalDate toDate);

  List<MerchantInvoiceEntity> findByStoreIdAndAndCountryCode(String storeId, String countryCode);

  @Modifying
  @Query("UPDATE MerchantInvoiceEntity m SET m.filePath = :filePath WHERE m.id = :MerchantInvoiceEntityId")
  int updateMerchantInvoiceEntity(long MerchantInvoiceEntityId, String filePath);

  MerchantInvoiceEntity findByInvoiceNumber(String invoiceNumber);

  int countByCountryCodeAndInvoiceNumberContaining(String countryCode, String invoiceNumberPattern);
}
