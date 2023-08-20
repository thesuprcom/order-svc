package com.supr.orderservice.service.impl;

import com.supr.orderservice.entity.AccountingOrderItemEntity;
import com.supr.orderservice.enums.ExternalStatus;
import com.supr.orderservice.repository.AccountingOrderItemEntityRepository;
import com.supr.orderservice.service.AccountingOrderItemDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountingOrderItemDetailsServiceImpl implements AccountingOrderItemDetailsService {

    private final AccountingOrderItemEntityRepository accountingOrderItemDetailsRepository;

    @Override
    public List<AccountingOrderItemEntity> findByStoreId(String storeId) {
        return accountingOrderItemDetailsRepository.findByStoreId(storeId);
    }

    @Override
    public AccountingOrderItemEntity insertItemDetails(AccountingOrderItemEntity accountingOrderItemDetails) {
        return accountingOrderItemDetailsRepository.save(accountingOrderItemDetails);
    }

    @Override
    public AccountingOrderItemEntity updateAccountingItemDetails(AccountingOrderItemEntity accountingOrderItemDetails) {
        return accountingOrderItemDetailsRepository.save(accountingOrderItemDetails);
    }

    @Override
    public List<AccountingOrderItemEntity> insertOrderItemList(
            List<AccountingOrderItemEntity> accountingOrderItemDetails) {
        return accountingOrderItemDetailsRepository.saveAll(accountingOrderItemDetails);
    }

    @Override
    public List<AccountingOrderItemEntity> fetchOrderListByStoreAndRangeAndStatus(
            String storeId, Date startTime, Date endTime, List<ExternalStatus> externalStatusList) {
        return accountingOrderItemDetailsRepository
                .findByStoreIdAndCreatedAtBetweenAndExternalStatusIn(storeId, startTime, endTime,
                        externalStatusList);
    }

    @Override
    public List<AccountingOrderItemEntity>
    fetchOrderListByMerchantAndRangeAndStatus(String merchantId, Date startTime, Date endTime,
                                              List<ExternalStatus> externalStatusList) {
        return null;
    }

    @Override
    public List<AccountingOrderItemEntity> fetchOrderListByStoreAndRangeAndStatusForInvoice(
            String storeId, Date startTime, Date endTime, List<ExternalStatus> externalStatusList) {
        return accountingOrderItemDetailsRepository
                .findByStoreIdAndCreatedAtBetweenAndExternalStatusInAndInvoiceNumberIsNull(storeId, startTime, endTime,
                        externalStatusList);
    }

    @Override
    public List<AccountingOrderItemEntity> fetchOrderListByStatusAndRange(List<ExternalStatus> externalStatusList,
                                                                          Date startTime, Date endTime) {
        return accountingOrderItemDetailsRepository
                .findByExternalStatusInAndCreatedAtBetween(externalStatusList, startTime, endTime);
    }

    @Override
    public List<AccountingOrderItemEntity> fetchOrderListByStoreAndRange(
            String storeId, Date startTime, Date endTime) {
        return accountingOrderItemDetailsRepository.findByStoreIdAndCreatedAtBetween(storeId, startTime, endTime);
    }

    @Override
    public List<AccountingOrderItemEntity> fetchOrderListByRange(
            Date startTime, Date endTime) {
        return accountingOrderItemDetailsRepository.findByCreatedAtBetween(startTime, endTime);
    }

    @Override
    public List<AccountingOrderItemEntity> fetchOrderItemListByOrderId(String orderId) {
        return accountingOrderItemDetailsRepository.findByOrderId(orderId);
    }

    @Override
    public List<AccountingOrderItemEntity> fetchOrderItemListByInvoiceId(String invoiceId) {
        return accountingOrderItemDetailsRepository.findByInvoiceNumber(invoiceId);
    }

    @Override
    public AccountingOrderItemEntity fetchByOrderItemId(String orderItemId) {
        return accountingOrderItemDetailsRepository.findByOrderItemId(orderItemId);
    }

}
