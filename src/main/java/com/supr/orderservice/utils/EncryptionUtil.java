package com.supr.orderservice.utils;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class EncryptionUtil {
  private final Charset charset = Charsets.UTF_8;
  private final Cipher cipher;

  @Value("${app.encryption.key}")
  private String key;

  @SneakyThrows
  public EncryptionUtil() {
    cipher = Cipher.getInstance("AES/GCM/NoPadding");
  }

  @SneakyThrows
  public String encrypt(String plainText) {
    if (Strings.isNullOrEmpty(plainText)) {
      return plainText;
    }

    SecretKey secretKey = new SecretKeySpec(key.getBytes(charset), "AES");

    SecureRandom secureRandom = new SecureRandom();
    byte[] iv = new byte[12];
    secureRandom.nextBytes(iv);

    GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);

    byte[] cipherText = cipher.doFinal(plainText.getBytes(charset));

    ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
    byteBuffer.put(iv);
    byteBuffer.put(cipherText);
    byte[] cipherMessage = byteBuffer.array();

    return Base64.getEncoder().encodeToString(cipherMessage);
  }

  @SneakyThrows
  public String decrypt(String encryptedText) {
    if (Strings.isNullOrEmpty(encryptedText)) {
      return encryptedText;
    }

    SecretKey secretKey = new SecretKeySpec(key.getBytes(charset), "AES");
    byte[] cipherText = Base64.getDecoder().decode(encryptedText);
    GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, cipherText, 0, 12);
    cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);

    byte[] plainText = cipher.doFinal(cipherText, 12, cipherText.length - 12);

    return new String(plainText, charset);
  }
}
