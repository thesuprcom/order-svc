package com.supr.orderservice.utils;

import com.supr.orderservice.entity.OrderEntity;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenUtility {
    @Value("${order-secret-key}")
    private String secretKey;


    @SneakyThrows
    public String createTokenForPayment(OrderEntity order) {
        String data = order.getOrderId() + "#" + order.getUserId();
        SecretKey secretKey = generateSecretKey(this.secretKey);
        return encryptValue(data, secretKey);
    }

    public SecretKey generateSecretKey(String secretKeyString) {
        byte[] keyBytes = secretKeyString.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, "AES");
    }

    public String encryptValue(String value, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decryptValue(String encryptedValue, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
