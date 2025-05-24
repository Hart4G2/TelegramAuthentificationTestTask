package com.testtask.tesktask.controller;

import com.testtask.tesktask.entity.TelegramUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        TelegramUser user = (TelegramUser) session.getAttribute("user");
        model.addAttribute("user", user);
        return "index";
    }
}
