package com.testtask.tesktask.model;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TelegramAuthDataTest {

    @Test
    public void testCreateAuthDataWithAllValues() {
        Map<String, String> data = new HashMap<>();
        data.put("id", "12345");
        data.put("first_name", "John");
        data.put("last_name", "Doe");
        data.put("username", "johndoe");
        data.put("photo_url", "http://example.com/photo.jpg");
        data.put("auth_date", "1610000000");
        data.put("hash", "somehashvalue");

        TelegramAuthData authData = TelegramAuthData.createAuthData(data);

        assertNotNull(authData);
        assertEquals(12345L, authData.getId());
        assertEquals("John", authData.getFirstName());
        assertEquals("Doe", authData.getLastName());
        assertEquals("johndoe", authData.getUsername());
        assertEquals("http://example.com/photo.jpg", authData.getPhotoUrl());
        assertEquals(1610000000L, authData.getAuthDate());
        assertEquals("somehashvalue", authData.getHash());
    }

    @Test
    public void testCreateAuthDataWithMissingOptionalValues() {
        Map<String, String> data = new HashMap<>();
        data.put("id", "67890");
        data.put("auth_date", "1610000001");
        // first_name, last_name, username, photo_url, hash отсутствуют

        TelegramAuthData authData = TelegramAuthData.createAuthData(data);

        assertNotNull(authData);
        assertEquals(67890L, authData.getId());
        assertNull(authData.getFirstName());
        assertNull(authData.getLastName());
        assertNull(authData.getUsername());
        assertNull(authData.getPhotoUrl());
        assertEquals(1610000001L, authData.getAuthDate());
        assertNull(authData.getHash());
    }

    @Test
    public void testCreateAuthDataWithInvalidLongValue() {
        Map<String, String> data = new HashMap<>();
        data.put("id", "not-a-number");
        data.put("auth_date", "1610000000");

        Exception exception = assertThrows(NumberFormatException.class, () -> {
            TelegramAuthData.createAuthData(data);
        });

        String expectedMessage = "For input string";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}