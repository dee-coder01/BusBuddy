package com.travellers_apis.nomadic_bus.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travellers_apis.nomadic_bus.commons.NoSessionFoundException;
import com.travellers_apis.nomadic_bus.models.UserSession;
import com.travellers_apis.nomadic_bus.repositories.UserSessionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSessionService {
    final UserSessionRepository sessionRepository;

    @Transactional(readOnly = true)
    UserSession findSessionByUserId(Long userID) {
        return sessionRepository.findByUserID(userID)
                .orElseThrow(() -> new NoSessionFoundException("Session not found for the user, Please login."));
    }

    @Transactional(readOnly = true)
    boolean validateUserKey(String userKey) {
        return sessionRepository.findByUuid(userKey).isPresent();
    }

    @Transactional(readOnly = true)
    UserSession findSessionByUserKey(String userKey) {
        return sessionRepository.findByUuid(userKey)
                .orElseThrow(() -> new NoSessionFoundException("Invalid user key."));
    }

    @Transactional
    public boolean deleteUserSession(Long userId) {
        return sessionRepository.deleteByUserID(userId);
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
