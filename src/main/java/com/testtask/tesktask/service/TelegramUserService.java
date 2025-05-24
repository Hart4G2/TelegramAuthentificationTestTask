package com.testtask.tesktask.service;

import com.testtask.tesktask.TelegramUserRepository;
import com.testtask.tesktask.entity.TelegramUser;
import com.testtask.tesktask.model.TelegramAuthData;
import org.springframework.stereotype.Service;

@Service
public class TelegramUserService {

    private final TelegramUserRepository repository;

    public TelegramUserService(TelegramUserRepository repository) {
        this.repository = repository;
    }

    public TelegramUser saveOrUpdate(TelegramAuthData authData) {
        TelegramUser user = repository.findById(authData.getId()).orElse(new TelegramUser());
        user.setId(authData.getId());
        user.setFirstName(authData.getFirstName());
        user.setLastName(authData.getLastName());
        user.setUsername(authData.getUsername());
        user.setPhotoUrl(authData.getPhotoUrl());
        user.setAuthDate(authData.getAuthDate());
        return repository.save(user);
    }
}