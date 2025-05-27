package com.testtask.tesktask.controller;

import com.testtask.tesktask.model.TelegramAuthData;
import com.testtask.tesktask.service.TelegramAuthService;
import com.testtask.tesktask.entity.TelegramUser;
import com.testtask.tesktask.service.TelegramUserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final TelegramAuthService telegramAuthService;
    private final TelegramUserService telegramUserService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody String telegramAnswer, HttpSession session) {
        Map<String, String> initData = parseInitData(telegramAnswer);

        if (!telegramAuthService.verifyTelegramWebAppData(initData)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Неверная аутентификация"));
        }

        Map<String, String> userData = parseUserData(initData.get("user"));

        TelegramAuthData authData = TelegramAuthData.createAuthData(userData);
        TelegramUser user = telegramUserService.saveOrUpdate(authData);
        session.setAttribute("user", user);
        return ResponseEntity.ok(Collections.singletonMap("redirect", "/"));
    }

    private Map<String, String> parseUserData(String userData) {
        Map<String, String> user = new HashMap<>();

        userData = userData.substring(1, userData.length() - 1);

        userData = userData.replace("\\/", "/").replace("\"", "");

        String[] pairs = userData.split(",");

        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            if (keyValue.length == 2) {
                user.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }

        return user;
    }

    private Map<String, String> parseInitData(String initData) {
        Map<String, String> params = new TreeMap<>();

        for (String pair : initData.split("&")) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                if (key.startsWith("{\"initData\":\"user")) {
                    key = "user";
                }
                params.put(key, value);
            }
        }
        return params;
    }
}