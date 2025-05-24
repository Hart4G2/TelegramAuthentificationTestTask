package com.testtask.tesktask.model;

import lombok.Getter;
import lombok.Setter;

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

}