package com.travellers_apis.nomadic_bus.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travellers_apis.nomadic_bus.commons.AdminException;
import com.travellers_apis.nomadic_bus.models.Admin;
import com.travellers_apis.nomadic_bus.models.LoginCredential;
import com.travellers_apis.nomadic_bus.models.AdminSession;
import com.travellers_apis.nomadic_bus.repositories.AdminRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminLoginService {
    final AdminRepository repo;
    final AdminSessionService sessionService;

    @Transactional
    public AdminSession createAdminSession(String adminName) {
        Admin admin = repo.findByEmail(adminName).orElse(null);
        AdminSession session = new AdminSession();
        session.setAdmin(admin);
        session.setTime(LocalDateTime.now());
        session.setUuid(UUID.randomUUID().toString());
        return sessionService.createNewSession(session);
    }

    @Transactional
    public void logOutAdmin(String userKey) {
        sessionService.deleteAdminSessionByAdminKey(userKey);
    }

    @Transactional(readOnly = true)
    public Admin findAdminByEmailAndPassword(LoginCredential credential) {
        return repo.findByEmailAndPassword(credential.getEmail(), credential.getPassword())
                .orElseThrow(() -> new AdminException("Check your credential."));
    }

    @Transactional(readOnly = true)
    public Admin findUserWithUserName(String userName) {
        return repo.findByEmail(userName).orElse(null);
    }
}
