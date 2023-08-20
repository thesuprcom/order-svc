package com.supr.orderservice.service.external;

import com.supr.orderservice.model.DeliveryFeeInfo;
import com.supr.orderservice.model.request.WalletCreateRequest;
import com.supr.orderservice.model.request.WalletUpdateRequest;
import com.supr.orderservice.model.response.WalletResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.math.BigDecimal;

import static com.supr.orderservice.utils.Constants.X_COUNTRY_CODE_HEADER_KEY;

@FeignClient(name = "wallet-service", url = "${wallet.base-url}")
public interface WalletServiceClient {
    String COUNTRY_CODE_HEADER = "X-Country-Code";
    String CURRENCY_CODE_HEADER = "X-Currency-Code";

    @GetMapping("/api/v1/internal/wallet/get/{user-id}")
    WalletResponse fetchWalletDetails(@PathVariable("user-id") String userId);

    @PostMapping("/api/v1/internal/wallet/create")
    WalletResponse createWallet(@Valid @RequestBody WalletCreateRequest walletCreateRequest,
                                @RequestHeader(value = COUNTRY_CODE_HEADER) String countryCode,
                                @RequestHeader(value = CURRENCY_CODE_HEADER) String currencyCode);

    @PostMapping("/api/v1/internal/wallet/update")
    WalletResponse updateWallet(@Valid @RequestBody WalletUpdateRequest walletUpdateRequest,
                                @RequestHeader(value = COUNTRY_CODE_HEADER) String countryCode,
                                @RequestHeader(value = CURRENCY_CODE_HEADER) String currencyCode);

}
