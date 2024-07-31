package com.travellers_apis.nomadic_bus.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travellers_apis.nomadic_bus.commons.NoSessionFoundException;
import com.travellers_apis.nomadic_bus.commons.UserException;
import com.travellers_apis.nomadic_bus.models.User;
import com.travellers_apis.nomadic_bus.models.UserSession;
import com.travellers_apis.nomadic_bus.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserLoginService {
    final UserRepository repository;
    final UserSessionService sessionService;

    @Transactional
    public String validateUserCredential(String userName) {
        User user = repository.findByEmail(userName).orElseThrow(() -> new UserException("Invalid login credentials."));
        UserSession session = new UserSession();
        session.setUser(user);
        session.setTime(LocalDateTime.now());
        session.setUuid(UUID.randomUUID().toString());
        return sessionService.createNewSession(session).getUuid();
    }

    @Transactional
    public void logOutUser(String userKey) {
        try {
            UserSession currentSession = sessionService.findSessionByUserKey(userKey)
                    .orElseThrow(() -> new NoSessionFoundException("No session found with user key " + userKey));
            sessionService.deleteUserSession(currentSession.getUser().getId());
        } catch (NoSessionFoundException e) {
            throw new UserException("Session is not found in the system.");
        }
    }

    @Transactional(readOnly = true)
    public Optional<User> findUserWithUserName(String userName) {
        return repository.findByEmail(userName);
    }

    @Transactional(readOnly = true)
    public boolean userExistsWithUserName(String userName) {
        return repository.findByEmail(userName).isPresent();
    }

    @Transactional(readOnly = true)
    public Optional<User> findUserWithUserId(Long userId) {
        return repository.findById(userId);
    }

    @Transactional(readOnly = true)
    public boolean userExistsWithUserId(Long userId) {
        return repository.findById(userId).isPresent();
    }
}
