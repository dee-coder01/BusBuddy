package com.travellers_apis.nomadic_bus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.travellers_apis.nomadic_bus.models.User;
import com.travellers_apis.nomadic_bus.repositories.UserRepo;

import jakarta.transaction.Transactional;

@Service
public class UserSignUpService {
    @Autowired
    UserRepo repository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserLoginService userLoginService;

    @Transactional
    public boolean userSignUp(User userDetails) {
        try {
            userLoginService.findUserWithUserName(userDetails.getEmail());
            return false;
        } catch (UsernameNotFoundException e) {
            userDetails.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            User user = repository.save(userDetails);
            System.out.println(userDetails.toString());
            Long userId = user.getUserID();
            if (userId == null)
                return false;
            return true;
        }
    }

    public boolean deleteUser(User userDetails) {
        int users = repository.deleteByEmailAndPassword(userDetails.getEmail(), userDetails.getPassword());
        if (users > 0)
            return true;
        else
            return false;
    }
}