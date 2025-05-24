package com.testtask.tesktask;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "telegram_user")
public class TelegramUser {

    @Id
    private Long id;

    private String firstName;
    private String lastName;
    private String username;
    private String photoUrl;
    private Long authDate;
}
