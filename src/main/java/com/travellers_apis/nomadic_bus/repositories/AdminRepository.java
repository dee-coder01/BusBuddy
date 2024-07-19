package com.travellers_apis.nomadic_bus.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travellers_apis.nomadic_bus.models.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmailAndPassword(String email, String password);

    Optional<Admin> findByEmail(String email);
}
