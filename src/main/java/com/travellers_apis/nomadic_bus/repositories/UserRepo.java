package com.travellers_apis.nomadic_bus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travellers_apis.nomadic_bus.models.User;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    public User findByUserID(Long id);

    public User findByEmailAndPassword(String email, String password);

    @Transactional
    public int deleteByEmailAndPassword(String email, String password);

    public User findByEmail(String email);
}
