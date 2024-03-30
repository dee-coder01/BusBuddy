package com.travellers_apis.nomadic_bus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travellers_apis.nomadic_bus.models.User;

@Repository
public interface UserRepo extends JpaRepository<User, String> {
    public User findByUserID(String id);
}
