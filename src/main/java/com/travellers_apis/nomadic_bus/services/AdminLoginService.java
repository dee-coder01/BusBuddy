package com.travellers_apis.nomadic_bus.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        AdminSession adminSession = repo.findByEmail(adminName)
                .map(this::sessionMapper)
                .orElseThrow(() -> new UsernameNotFoundException("No admin found with " + adminName + " username."));
        return sessionService.createNewSession(adminSession);
    }

    @Transactional
    public void logOutAdmin(String userKey) {
        sessionService.deleteAdminSessionByAdminKey(userKey);
    }

    @Transactional(readOnly = true)
    public Optional<Admin> findAdminByEmailAndPassword(LoginCredential credential) {
        return repo.findByEmailAndPassword(credential.getEmail(), credential.getPassword());
    }

    @Transactional(readOnly = true)
    public Admin findUserWithUserName(String userName) {
        return repo.findByEmail(userName).orElse(null);
    }

    private AdminSession sessionMapper(Admin adminSession) {
        AdminSession session = new AdminSession();
        session.setAdmin(adminSession);
        session.setTime(LocalDateTime.now());
        session.setUuid(UUID.randomUUID().toString());
        return session;
    }
}
