package com.travellers_apis.nomadic_bus.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travellers_apis.nomadic_bus.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserID(Long id);

    Optional<User> findByEmailAndPassword(String email, String password);

    int deleteByEmailAndPassword(String email, String password);

    Optional<User> findByEmail(String email);
}
