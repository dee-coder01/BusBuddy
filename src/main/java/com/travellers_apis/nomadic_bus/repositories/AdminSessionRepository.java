package com.travellers_apis.nomadic_bus.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.travellers_apis.nomadic_bus.models.AdminSession;

public interface AdminSessionRepository extends JpaRepository<AdminSession, Long> {
    Integer deleteByAdminId(Long userID);

    Integer deleteByUuid(String userKey);

    Optional<AdminSession> findByAdminId(Long userID);

    Optional<AdminSession> findByUuid(String userKey);
}
