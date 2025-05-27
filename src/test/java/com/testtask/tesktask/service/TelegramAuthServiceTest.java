package com.testtask.tesktask.service;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.Test;

public class TelegramAuthServiceTest {

    @Test
    public void testVerifyTelegramWebAppData_withValidData() {
        LinkedHashMap<String, String> initData = new LinkedHashMap<>();
        initData.put("auth_date", "1748360582");
        initData.put("chat_instance", "3585881437241802036");
        initData.put("chat_type", "private");
        initData.put("signature", "0jHMJgBvoSMY3ng0gsyl4fRi1zgJK2k9tx5qu_BWzMgYWQtuIxxxLs6pWCgFQmoe7QBKeYyqLAoKRrG9xt6iDA");
        initData.put("user", "{\"id\":919056040,\"first_name\":\"Артём\",\"last_name\":\"Буевич\",\"username\":\"hart4g2\",\"language_code\":\"ru\",\"allows_write_to_pm\":true,\"photo_url\":\"https://t.me/i/userpic/320/MU-IkYgrT7izZv3pPSxn477QAaZPTEj1XqDtk4q70KU.svg\"}");

        String validHash = computeValidHash(initData);
        initData.put("hash", validHash);

        TelegramAuthService service = new TelegramAuthService();
        boolean isValid = service.verifyTelegramWebAppData(new LinkedHashMap<>(initData));
        assertTrue(isValid);
    }

    @Test
    public void testVerifyTelegramWebAppData_withInvalidHash() {
        LinkedHashMap<String, String> initData = new LinkedHashMap<>();
        initData.put("auth_date", "1748360582");
        initData.put("chat_instance", "3585881437241802036");
        initData.put("chat_type", "private");
        initData.put("signature", "0jHMJgBvoSMY3ng0gsyl4fRi1zgJK2k9tx5qu_BWzMgYWQtuIxxxLs6pWCgFQmoe7QBKeYyqLAoKRrG9xt6iDA");
        initData.put("user", "{\"id\":919056040,\"first_name\":\"Артём\",\"last_name\":\"Буевич\",\"username\":\"hart4g2\",\"language_code\":\"ru\",\"allows_write_to_pm\":true,\"photo_url\":\"https://t.me/i/userpic/320/MU-IkYgrT7izZv3pPSxn477QAaZPTEj1XqDtk4q70KU.svg\"}");

        initData.put("hash", "invalidhash");

        TelegramAuthService service = new TelegramAuthService();
        boolean isValid = service.verifyTelegramWebAppData(new LinkedHashMap<>(initData));
        assertFalse(isValid);
    }

    // ===== Помощники для вычисления хэша (повторение логики сервиса) =====

    private String computeValidHash(Map<String, String> initData) {
        LinkedHashMap<String, String> copy = new LinkedHashMap<>(initData);
        copy.remove("hash");
        String dataCheckString = buildDataCheckString(copy);
        byte[] secretKeyBytes = generateHMACBytes("8090162327:AAHS6tDcYd2MqCz5Z8jK2QABgqDUSQTid28", "WebAppData");
        byte[] calcBytes = generateHMACBytes(dataCheckString, secretKeyBytes);
        return bytesToHex(calcBytes);
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

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
