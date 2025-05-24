package com.testtask.tesktask;

import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TelegramAuthService {

    private final String botToken = "7876754513:AAEtnUIHtSuwj2I-CDJ1-IOOj98f26nvtAQ"; // bot API

    public boolean isValidAuthData(TelegramAuthData authData) {
        Map<String, String> dataCheck = getStringStringMap(authData);

        List<String> dataCheckArr = dataCheck.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.toList());
        String dataCheckString = String.join("\n", dataCheckArr);

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] key = digest.digest(botToken.getBytes(StandardCharsets.UTF_8));

            Mac sha256HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(key, "HmacSHA256");
            sha256HMAC.init(secretKey);
            byte[] hashBytes = sha256HMAC.doFinal(dataCheckString.getBytes(StandardCharsets.UTF_8));

            StringBuilder hashBuilder = new StringBuilder();
            for (byte b : hashBytes) {
                hashBuilder.append(String.format("%02x", b));
            }
            String computedHash = hashBuilder.toString();

            return computedHash.equals(authData.getHash());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Map<String, String> getStringStringMap(TelegramAuthData authData) {
        Map<String, String> dataCheck = new HashMap<>();
        if (authData.getId() != null) dataCheck.put("id", authData.getId().toString());
        if (authData.getFirstName() != null) dataCheck.put("first_name", authData.getFirstName());
        if (authData.getLastName() != null) dataCheck.put("last_name", authData.getLastName());
        if (authData.getUsername() != null) dataCheck.put("username", authData.getUsername());
        if (authData.getPhotoUrl() != null) dataCheck.put("photo_url", authData.getPhotoUrl());
        if (authData.getAuthDate() != null) dataCheck.put("auth_date", authData.getAuthDate().toString());
        return dataCheck;
    }
}

