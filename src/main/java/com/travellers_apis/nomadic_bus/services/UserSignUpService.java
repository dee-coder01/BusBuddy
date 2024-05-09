package com.travellers_apis.nomadic_bus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travellers_apis.nomadic_bus.models.User;
import com.travellers_apis.nomadic_bus.repositories.UserRepo;

@Service
public class UserSignUpService {
    @Autowired
    UserRepo repository;

    public boolean userSignUp(User userDetails) {
        User user = repository.save(userDetails);
        Long userId = user.getUserID();
        if (userId == null)
            return false;
        return true;
    }
}