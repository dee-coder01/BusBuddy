package com.travellers_apis.nomadic_bus.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travellers_apis.nomadic_bus.models.UserSession;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    boolean deleteByUserID(Long userID);

    boolean deleteByUuid(String userKey);

    Optional<UserSession> findByUserID(Long userID);

    Optional<UserSession> findByUuid(String userKey);
}
