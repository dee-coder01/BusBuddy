package com.travellers_apis.nomadic_bus.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travellers_apis.nomadic_bus.commons.AdminException;
import com.travellers_apis.nomadic_bus.models.Admin;
import com.travellers_apis.nomadic_bus.models.LoginCredential;
import com.travellers_apis.nomadic_bus.models.UserSession;
import com.travellers_apis.nomadic_bus.repositories.AdminRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminLoginService {
    final AdminRepo repo;
    final UserSessionService sessionService;

    @Transactional
    public UserSession validateAdminCredential(LoginCredential credential) {
        Admin admin = repo.findByEmailAndPassword(credential.getEmail(), credential.getPassword())
                .orElseThrow(() -> new AdminException("Check your credential."));
        UserSession session = new UserSession();
        session.setUserID(admin.getId());
        session.setTime(LocalDateTime.now());
        session.setUuid(UUID.randomUUID().toString());
        sessionService.createNewSession(session);
        return session;
    }

    @Transactional
    public void logOutAdmin(String userKey) {
        sessionService.deleteUserSessionByUserKey(userKey);
    }
}
