package com.supr.orderservice.service.external;

import com.supr.orderservice.model.ItemInfo;
import com.supr.orderservice.model.request.CheckItemDetailsRequest;
import com.supr.orderservice.model.request.UpdateItemStatusRequest;
import com.supr.orderservice.model.request.UpdateQuantityRequest;
import com.supr.orderservice.model.response.ProductDataResponse;
import com.supr.orderservice.model.response.SellerSkuResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.supr.orderservice.utils.Constants.STORE_ID_HEADER;

@FeignClient(name = "store-catalog-write-service", url = "${catalog.base-url}")
public interface InventoryServiceClient {


    @PutMapping("/catalog/qty_update")
    void updateQuantity(@RequestHeader(STORE_ID_HEADER) String storeId, UpdateQuantityRequest request);

    @PutMapping("/catalog/store-item-status-update")
    void updateStoreItemStatus(@RequestHeader(STORE_ID_HEADER) String storeId, UpdateItemStatusRequest request);

    @GetMapping("/catalog/item-detail/{item-id}")
    ItemInfo fetchItemDetails(@RequestHeader(STORE_ID_HEADER) String storeId,
                              @PathVariable(name = "item-id") String pskuCode);

    @GetMapping("/catalog/item-detail/{brand-id}/{total-amount}")
    List<ItemInfo> fetchItemDetailForSwap(@PathVariable(name = "brand-id") String brandId,
                                          @PathVariable(name = "total-amount") String totalAmount,
                                          @RequestParam(value = "page", defaultValue = "0") int page,
                                          @RequestParam(value = "size", defaultValue = "20") int size);

    @PostMapping("/api/v1/{country-code}/product/check-stock/")
    ProductDataResponse fetchSellerSkuDetails(@PathVariable(name = "country-code") String countryCode,
                                              @RequestBody CheckItemDetailsRequest checkItemDetailsRequest);

}
