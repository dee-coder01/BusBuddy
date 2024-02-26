package com.travellers_apis.nomadic_bus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travellers_apis.nomadic_bus.models.UserSession;

import jakarta.transaction.Transactional;

@Repository
public interface UserLoginRepo extends JpaRepository<UserSession, String> {

    @Transactional
    void deleteByUserID(String userID);

    UserSession findByUserID(String userID);
}
