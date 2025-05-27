package com.testtask.tesktask.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TelegramAuthData {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String photoUrl;
    private Long authDate;
    private String hash;

    public static TelegramAuthData createAuthData(Map<String, String> data) {
        TelegramAuthData authData = new TelegramAuthData();

        authData.id = data.containsKey("id") ? Long.parseLong(data.get("id")) : null;
        authData.firstName = data.getOrDefault("first_name", null);
        authData.lastName = data.getOrDefault("last_name", null);
        authData.username = data.getOrDefault("username", null);
        authData.photoUrl = data.getOrDefault("photo_url", null);
        authData.authDate = data.containsKey("auth_date") ? Long.parseLong(data.get("auth_date")) : null;
        authData.hash = data.getOrDefault("hash", null);

        return authData;
    }
}