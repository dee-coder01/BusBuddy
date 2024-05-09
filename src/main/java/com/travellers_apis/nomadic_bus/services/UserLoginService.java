package com.travellers_apis.nomadic_bus.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.travellers_apis.nomadic_bus.dtos.UserSessionDTO;
import com.travellers_apis.nomadic_bus.models.LoginCredential;
import com.travellers_apis.nomadic_bus.models.User;
import com.travellers_apis.nomadic_bus.models.UserSession;
import com.travellers_apis.nomadic_bus.repositories.UserLoginRepo;
import com.travellers_apis.nomadic_bus.repositories.UserRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserLoginService {
    private UserRepo repo;
    private UserLoginRepo loginRepo;

    public UserSessionDTO validateUserCredential(LoginCredential credential) {
        User user = repo.findByEmailAndPassword(credential.getEmail(), credential.getPassword());
        if (user == null)
            return null;
        UserSession session = new UserSession();
        session.setUserID(user.getUserID());
        session.setTime(LocalDateTime.now());
        session.setUuid(UUID.randomUUID().toString());
        loginRepo.save(session);
        return new UserSessionDTO(session.getUuid());
    }

    public boolean logOutUser(String userKey) {
        UserSession currentSession = loginRepo.findByUuid(userKey);
        if (currentSession == null) {
            return false;
        }
        loginRepo.deleteByUserID(currentSession.getUserID());
        return true;
    }
}
