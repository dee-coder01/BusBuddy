package com.travellers_apis.nomadic_bus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travellers_apis.nomadic_bus.models.Admin;

@Repository
public interface AdminRepo extends JpaRepository<Admin, Long> {
    Admin findByEmailAndPassword(String email, String password);
}
