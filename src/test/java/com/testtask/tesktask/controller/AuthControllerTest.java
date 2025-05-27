package com.testtask.tesktask.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.testtask.tesktask.entity.TelegramUser;
import com.testtask.tesktask.model.TelegramAuthData;
import com.testtask.tesktask.service.TelegramAuthService;
import com.testtask.tesktask.service.TelegramUserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TelegramAuthService telegramAuthService;

    @MockitoBean
    private TelegramUserService telegramUserService;

    @Test
    public void testLoginUnauthorized() throws Exception {
        String requestBody = "auth_date=1748360582&chat_instance=3585881437241802036&chat_type=private" +
                "&signature=0jHMJgBvoSMY3ng0gsyl4fRi1zgJK2k9tx5qu_BWzMgYWQtuIxxxLs6pWCgFQmoe7QBKeYyqLAoKRrG9xt6iDA" +
                "&user=%7B%22id%22%3A919056040%2C%22first_name%22%3A%22Артём%22%2C%22last_name%22%3A%22Буевич%22" +
                "%2C%22username%22%3A%22hart4g2%22%2C%22language_code%22%3A%22ru%22%2C%22allows_write_to_pm%22%3Atrue" +
                "%2C%22photo_url%22%3A%22https%3A%2F%2Ft.me%2Fi%2Fuserpic%2F320%2FMU-IkYgrT7izZv3pPSxn477QAaZPTEj1XqDtk4q70KU.svg%22%7D" +
                "&hash=invalidhash";

        when(telegramAuthService.verifyTelegramWebAppData(anyMap())).thenReturn(false);

        mockMvc.perform(post("/auth/login")
                        .content(requestBody)
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Неверная аутентификация"));
    }

    @Test
    public void testLoginSuccess() throws Exception {
        String requestBody = "auth_date=1748360582&chat_instance=3585881437241802036&chat_type=private" +
                "&signature=0jHMJgBvoSMY3ng0gsyl4fRi1zgJK2k9tx5qu_BWzMgYWQtuIxxxLs6pWCgFQmoe7QBKeYyqLAoKRrG9xt6iDA" +
                "&user=%7B%22id%22%3A919056040%2C%22first_name%22%3A%22Артём%22%2C%22last_name%22%3A%22Буевич%22" +
                "%2C%22username%22%3A%22hart4g2%22%2C%22language_code%22%3A%22ru%22%2C%22allows_write_to_pm%22%3Atrue" +
                "%2C%22photo_url%22%3A%22https%3A%2F%2Ft.me%2Fi%2Fuserpic%2F320%2FMU-IkYgrT7izZv3pPSxn477QAaZPTEj1XqDtk4q70KU.svg%22%7D" +
                "&hash=validhash";

        when(telegramAuthService.verifyTelegramWebAppData(anyMap())).thenReturn(true);

        TelegramUser user = new TelegramUser();
        user.setId(919056040L);
        user.setFirstName("Артём");
        user.setLastName("Буевич");
        user.setUsername("hart4g2");
        user.setPhotoUrl("https://t.me/i/userpic/320/MU-IkYgrT7izZv3pPSxn477QAaZPTEj1XqDtk4q70KU.svg");
        user.setAuthDate(1748360582L);

        when(telegramUserService.saveOrUpdate(any(TelegramAuthData.class))).thenReturn(user);

        mockMvc.perform(post("/auth/login")
                        .content(requestBody)
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.redirect").value("/"))
                .andDo(result -> {
                    // Дополнительно проверяем, что в сессии установлен пользователь.
                    HttpSession session = result.getRequest().getSession(false);
                    assertNotNull(session);
                    TelegramUser sessionUser = (TelegramUser) session.getAttribute("user");
                    assertNotNull(sessionUser);
                    assertEquals(919056040L, sessionUser.getId());
                });
    }
}

