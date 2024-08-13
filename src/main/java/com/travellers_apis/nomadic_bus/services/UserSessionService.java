package com.travellers_apis.nomadic_bus.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travellers_apis.nomadic_bus.models.UserSession;
import com.travellers_apis.nomadic_bus.repositories.UserSessionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSessionService {
    final UserSessionRepository sessionRepository;

    @Transactional(readOnly = true)
    public Optional<UserSession> findSessionByUserId(Long userID) {
        return sessionRepository.findByUserId(userID);
    }

    @Transactional(readOnly = true)
    public boolean validateUserKey(String userKey) {
        return sessionRepository.findByUuid(userKey).isPresent();
    }

    @Transactional(readOnly = true)
    public Optional<UserSession> findSessionByUserKey(String userKey) {
        return sessionRepository.findByUuid(userKey);
    }

    @Transactional
    public boolean deleteUserSession(Long userId) {
        return sessionRepository.deleteByUserId(userId);
    }

    @Transactional
    public boolean deleteUserSessionByUserKey(String userKey) {
        return sessionRepository.deleteByUuid(userKey);
    }

    @Transactional
    public UserSession createNewSession(UserSession session) {
        return sessionRepository.save(session);
    }
}
