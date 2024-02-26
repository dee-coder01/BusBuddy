package com.travellers_apis.nomadic_bus.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.travellers_apis.nomadic_bus.models.Admin;
import com.travellers_apis.nomadic_bus.models.LoginCredential;
import com.travellers_apis.nomadic_bus.models.UserSession;
import com.travellers_apis.nomadic_bus.repositories.AdminRepo;
import com.travellers_apis.nomadic_bus.repositories.UserLoginRepo;

@Service
public class AdminLoginService {
    private AdminRepo repo;
    private UserLoginRepo loginRepo;

    public AdminLoginService(AdminRepo repo, UserLoginRepo loginRepo) {
        this.repo = repo;
        this.loginRepo = loginRepo;
    }

    public UserSession validateAdminCredential(LoginCredential credential) {
        Admin admin = repo.findByEmailAndPassword(credential.getEmail(), credential.getPassword());
        if (admin == null)
            return null;
        UserSession session = new UserSession();
        session.setUserID(admin.getId());
        session.setTime(LocalDateTime.now());
        session.setUuid(UUID.randomUUID().toString());
        loginRepo.save(session);
        return session;
    }

    public boolean logOutAdmin(UserSession session) {
        UserSession currentSession = loginRepo.findByUserID(session.getUserID());
        if (currentSession == null) {
            return false;
        }
        loginRepo.deleteByUserID(session.getUserID());
        return true;
    }
}
