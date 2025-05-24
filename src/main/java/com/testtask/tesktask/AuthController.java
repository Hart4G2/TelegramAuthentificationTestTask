package com.testtask.tesktask;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final TelegramAuthService telegramAuthService;

    public AuthController(TelegramAuthService telegramAuthService) {
        this.telegramAuthService = telegramAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody TelegramAuthData authData, HttpSession session) {
        if (!telegramAuthService.isValidAuthData(authData)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверная аутентификация");
        }
        session.setAttribute("user", authData);
        return ResponseEntity.ok("Пользователь: " + authData.getFirstName());
    }
}
