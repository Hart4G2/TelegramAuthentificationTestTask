package com.testtask.tesktask.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.testtask.tesktask.TelegramUserRepository;
import com.testtask.tesktask.entity.TelegramUser;
import com.testtask.tesktask.model.TelegramAuthData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TelegramUserServiceTest {

    @Mock
    private TelegramUserRepository repository;

    @InjectMocks
    private TelegramUserService service;

    @Test
    public void testSaveOrUpdate_whenUserExists() {
        Long userId = 1L;
        TelegramUser existingUser = new TelegramUser();
        existingUser.setId(userId);
        existingUser.setFirstName("OldName");
        existingUser.setLastName("OldLastName");
        existingUser.setUsername("old_username");
        existingUser.setPhotoUrl("http://example.com/old_photo.jpg");
        existingUser.setAuthDate(1000000000L);

        TelegramAuthData authData = new TelegramAuthData();
        authData.setId(userId);
        authData.setFirstName("NewName");
        authData.setLastName("Doe");
        authData.setUsername("new_username");
        authData.setPhotoUrl("http://example.com/new_photo.jpg");
        authData.setAuthDate(2000000000L);

        when(repository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(repository.save(any(TelegramUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TelegramUser result = service.saveOrUpdate(authData);

        verify(repository).findById(userId);
        verify(repository).save(existingUser);
        assertEquals(authData.getFirstName(), result.getFirstName());
        assertEquals(authData.getLastName(), result.getLastName());
        assertEquals(authData.getUsername(), result.getUsername());
        assertEquals(authData.getPhotoUrl(), result.getPhotoUrl());
        assertEquals(authData.getAuthDate(), result.getAuthDate());
    }

    @Test
    public void testSaveOrUpdate_whenUserNotFound() {
        Long userId = 2L;
        TelegramAuthData authData = new TelegramAuthData();
        authData.setId(userId);
        authData.setFirstName("NewUser");
        authData.setLastName("Smith");
        authData.setUsername("smith");
        authData.setPhotoUrl("http://example.com/photo.jpg");
        authData.setAuthDate(3000000000L);

        when(repository.findById(userId)).thenReturn(Optional.empty());
        when(repository.save(any(TelegramUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TelegramUser result = service.saveOrUpdate(authData);

        verify(repository).findById(userId);
        verify(repository).save(any(TelegramUser.class));
        assertEquals(authData.getId(), result.getId());
        assertEquals(authData.getFirstName(), result.getFirstName());
        assertEquals(authData.getLastName(), result.getLastName());
        assertEquals(authData.getUsername(), result.getUsername());
        assertEquals(authData.getPhotoUrl(), result.getPhotoUrl());
        assertEquals(authData.getAuthDate(), result.getAuthDate());
    }
}