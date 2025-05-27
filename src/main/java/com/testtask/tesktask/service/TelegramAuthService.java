package com.testtask.tesktask.service;

import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

@Service
public class TelegramAuthService {

    private final String BOT_TOKEN = "8090162327:AAHS6tDcYd2MqCz5Z8jK2QABgqDUSQTid28";

    public boolean verifyTelegramWebAppData(Map<String, String> initData) {
        String receivedHash = initData.remove("hash").replaceAll("[\"}]", "").trim();

        String dataCheckString = buildDataCheckString(initData);

        byte[] secretKeyBytes = generateHMACBytes(BOT_TOKEN, "WebAppData");
        byte[] calcBytes = generateHMACBytes(dataCheckString, secretKeyBytes);
        String calculatedHash = bytesToHex(calcBytes);

        return receivedHash.equals(calculatedHash);
    }

    private String buildDataCheckString(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        params.forEach((key, value) ->
                sb.append(key).append("=").append(value).append("\n")
        );
        return sb.toString().trim();
    }

    private byte[] generateHMACBytes(String message, byte[] key) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(keySpec);
            return mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("Ошибка генерации HMAC", e);
        }
    }

    private byte[] generateHMACBytes(String message, String key) {
        return generateHMACBytes(message, key.getBytes(StandardCharsets.UTF_8));
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
