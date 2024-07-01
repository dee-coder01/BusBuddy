package com.travellers_apis.nomadic_bus.services;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.travellers_apis.nomadic_bus.models.User;
import com.travellers_apis.nomadic_bus.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSignUpService {
    final UserRepository repository;
    final PasswordEncoder passwordEncoder;
    final UserLoginService userLoginService;

    @Transactional
    public boolean userSignUp(User userDetails) {
        try {
            userLoginService.findUserWithUserName(userDetails.getEmail());
            return false;
        } catch (UsernameNotFoundException e) {
            userDetails.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            repository.save(userDetails);
            return true;
        }
    }

    public boolean deleteUser(User userDetails) {
        int users = repository.deleteByEmailAndPassword(userDetails.getEmail(), userDetails.getPassword());
        return users > 0;
    }
}