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
    public UserSession findSessionByUserId(Long userID) {
        return sessionRepository.findByUserId(userID)
                .orElseThrow(() -> new NoSessionFoundException("Session not found for the user, Please login."));
    }

    @Transactional(readOnly = true)
    public boolean validateUserKey(String userKey) {
        return sessionRepository.findByUuid(userKey).isPresent();
    }

    @Transactional(readOnly = true)
    public UserSession findSessionByUserKey(String userKey) {
        return sessionRepository.findByUuid(userKey)
                .orElseThrow(() -> new NoSessionFoundException("Invalid user key."));
    }

    @Transactional
    public void deleteUserSession(Long userId) {
        sessionRepository.deleteById(userId);
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
