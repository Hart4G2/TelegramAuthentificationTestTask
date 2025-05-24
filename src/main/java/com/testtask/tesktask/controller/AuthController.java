package com.testtask.tesktask.controller;

import com.testtask.tesktask.model.TelegramAuthData;
import com.testtask.tesktask.service.TelegramAuthService;
import com.testtask.tesktask.entity.TelegramUser;
import com.testtask.tesktask.service.TelegramUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final TelegramAuthService telegramAuthService;
    private final TelegramUserService telegramUserService;

    public AuthController(TelegramAuthService telegramAuthService, TelegramUserService telegramUserService) {
        this.telegramAuthService = telegramAuthService;
        this.telegramUserService = telegramUserService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody TelegramAuthData authData, HttpSession session) {
        if (!telegramAuthService.isValidAuthData(authData)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Неверная аутентификация"));
        }
        TelegramUser user = telegramUserService.saveOrUpdate(authData);
        session.setAttribute("user", user);
        return ResponseEntity.ok(Collections.singletonMap("redirect", "/"));
    }
}