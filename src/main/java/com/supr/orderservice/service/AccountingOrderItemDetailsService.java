package com.supr.orderservice.service;

import com.supr.orderservice.entity.AccountingOrderItemEntity;
import com.supr.orderservice.enums.ExternalStatus;

import java.util.Date;
import java.util.List;

public interface AccountingOrderItemDetailsService {

  List<AccountingOrderItemEntity> findByStoreId(String storeId);

  AccountingOrderItemEntity insertItemDetails(AccountingOrderItemEntity accountingOrderItemDetails);

  AccountingOrderItemEntity updateAccountingItemDetails(AccountingOrderItemEntity accountingOrderItemDetails);

  List<AccountingOrderItemEntity> insertOrderItemList(List<AccountingOrderItemEntity> accountingOrderItemDetails);

  List<AccountingOrderItemEntity> fetchOrderListByStoreAndRangeAndStatus(String storeId, Date startTime, Date endTime,
                                                                          List<ExternalStatus> externalStatusList);

  List<AccountingOrderItemEntity> fetchOrderListByMerchantAndRangeAndStatus(String merchantId, Date startTime,
                                                                             Date endTime,
                                                                             List<ExternalStatus> externalStatusList);

  List<AccountingOrderItemEntity>
      fetchOrderListByStoreAndRangeAndStatusForInvoice(String storeId, Date startTime, Date endTime,
                                                       List<ExternalStatus> externalStatusList);

  List<AccountingOrderItemEntity> fetchOrderListByStatusAndRange(List<ExternalStatus> externalStatusList,
                                                                  Date startTime,
                                                                  Date endTime);

  List<AccountingOrderItemEntity> fetchOrderListByStoreAndRange(
      String storeId, Date startTime, Date endTime);

  List<AccountingOrderItemEntity> fetchOrderListByRange(
      Date startTime, Date endTime);

  List<AccountingOrderItemEntity> fetchOrderItemListByOrderId(String orderId);

  List<AccountingOrderItemEntity> fetchOrderItemListByInvoiceId(String invoiceId);

  AccountingOrderItemEntity fetchByOrderItemId(String orderItemId);
}
