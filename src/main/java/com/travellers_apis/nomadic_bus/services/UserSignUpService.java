package com.travellers_apis.nomadic_bus.services;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.travellers_apis.nomadic_bus.models.User;
import com.travellers_apis.nomadic_bus.repositories.UserRepo;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserSignUpService {
    UserRepo repository;
    PasswordEncoder passwordEncoder;
    UserLoginService userLoginService;

    @Transactional
    public boolean userSignUp(User userDetails) {
        try {
            userLoginService.findUserWithUserName(userDetails.getEmail());
            return false;
        } catch (UsernameNotFoundException e) {
            userDetails.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            User user = repository.save(userDetails);
            Long userId = user.getUserID();
            return userId != null;
        }
    }

    public boolean deleteUser(User userDetails) {
        int users = repository.deleteByEmailAndPassword(userDetails.getEmail(), userDetails.getPassword());
        return users > 0;
    }
}