package com.supr.orderservice.model.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.supr.orderservice.enums.WalletOperation;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class WalletUpdateRequest  implements Serializable {
    @NotEmpty(message = "Wallet id cannot be null")
    private String walletId;
    @NotEmpty(message = "Amount cannot be null")
    private String amount;
    private Boolean delete;

    private String orderId;
    @NotEmpty(message = "WalletOperation cannot be null")
    private WalletOperation walletOperation;


}