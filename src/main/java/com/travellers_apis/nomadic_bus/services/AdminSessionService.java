package com.travellers_apis.nomadic_bus.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travellers_apis.nomadic_bus.commons.NoSessionFoundException;
import com.travellers_apis.nomadic_bus.models.AdminSession;
import com.travellers_apis.nomadic_bus.repositories.AdminSessionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminSessionService {
    final AdminSessionRepository sessionRepository;

    @Transactional(readOnly = true)
    public AdminSession findSessionByAdminId(Long adminID) {
        return sessionRepository.findByAdminId(adminID)
                .orElseThrow(() -> new NoSessionFoundException("Session not found for the Admin, Please login."));
    }

    @Transactional(readOnly = true)
    public boolean validateAdminKey(String adminKey) {
        return sessionRepository.findByUuid(adminKey).isPresent();
    }

    @Transactional(readOnly = true)
    public AdminSession findSessionByAdminKey(String adminKey) {
        return sessionRepository.findByUuid(adminKey)
                .orElseThrow(() -> new NoSessionFoundException("Invalid Admin key."));
    }

    @Transactional
    public boolean deleteAdminSession(Long adminId) {
        return sessionRepository.deleteByAdminId(adminId);
    }

    @Transactional
    public boolean deleteAdminSessionByAdminKey(String adminKey) {
        return sessionRepository.deleteByUuid(adminKey);
    }

    @Transactional
    public AdminSession createNewSession(AdminSession session) {
        System.out.println(session);
        return sessionRepository.save(session);
    }
}
