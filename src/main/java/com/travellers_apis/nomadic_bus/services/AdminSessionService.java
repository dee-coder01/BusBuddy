package com.travellers_apis.nomadic_bus.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travellers_apis.nomadic_bus.models.AdminSession;
import com.travellers_apis.nomadic_bus.repositories.AdminSessionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminSessionService {
    final AdminSessionRepository sessionRepository;

    @Transactional(readOnly = true)
    public Optional<AdminSession> findSessionByAdminId(Long adminID) {
        return sessionRepository.findByAdminId(adminID);
    }

    @Transactional(readOnly = true)
    public boolean validateAdminKey(String adminKey) {
        return sessionRepository.findByUuid(adminKey).isPresent();
    }

    @Transactional(readOnly = true)
    public Optional<AdminSession> findSessionByAdminKey(String adminKey) {
        return sessionRepository.findByUuid(adminKey);
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
        return sessionRepository.save(session);
    }
}
