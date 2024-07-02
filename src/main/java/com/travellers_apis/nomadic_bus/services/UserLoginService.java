package com.travellers_apis.nomadic_bus.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travellers_apis.nomadic_bus.commons.NoSessionFoundException;
import com.travellers_apis.nomadic_bus.commons.UserException;
import com.travellers_apis.nomadic_bus.dtos.UserSessionDTO;
import com.travellers_apis.nomadic_bus.models.LoginCredential;
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
    public UserSessionDTO validateUserCredential(LoginCredential credential) {
        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
            return null;
        User user = repository.findByEmailAndPassword(credential.getEmail(), credential.getPassword()).orElse(null);
        if (user == null)
            throw new UserException("Invalid login credentials.");
        UserSession session = new UserSession();
        session.setUserID(user.getUserID());
        session.setTime(LocalDateTime.now());
        session.setUuid(UUID.randomUUID().toString());
        sessionService.createNewSession(session);
        return new UserSessionDTO(session.getUuid());
    }

    @Transactional
    public void logOutUser(String userKey) {
        try {
            UserSession currentSession = sessionService.findSessionByUserKey(userKey);
            sessionService.deleteUserSession(currentSession.getUserID());
        } catch (NoSessionFoundException e) {
            throw new UserException("Session is not found in the system.");
        }
    }

    @Transactional(readOnly = true)
    public User findUserWithUserName(String userName) {
        return repository.findByEmail(userName)
                .orElseThrow(() -> new UserException("User not found with username: " + userName));
    }

    @Transactional(readOnly = true)
    public boolean userExistsWithUserName(String userName) {
        return repository.findByEmail(userName).isPresent();
    }

    @Transactional(readOnly = true)
    public User findUserWithUserId(Long userId) {
        return repository.findById(userId).orElseThrow(() -> new UserException("Invalid user id"));
    }

    @Transactional(readOnly = true)
    public boolean userExistsWithUserId(Long userId) {
        return repository.findById(userId).isPresent();
    }
}
