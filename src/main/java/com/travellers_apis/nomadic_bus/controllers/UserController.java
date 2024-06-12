package com.travellers_apis.nomadic_bus.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travellers_apis.nomadic_bus.dtos.UserSessionDTO;
import com.travellers_apis.nomadic_bus.models.LoginCredential;
import com.travellers_apis.nomadic_bus.models.User;
import com.travellers_apis.nomadic_bus.services.UserLoginService;
import com.travellers_apis.nomadic_bus.services.UserSignUpService;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private UserLoginService service;
    private UserSignUpService signUpService;

    @PostMapping("/login")
    public ResponseEntity<UserSessionDTO> userLogin() {
        LoginCredential loginCredential = (LoginCredential) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return ResponseEntity.accepted().body(service.validateUserCredential(loginCredential));
    }

    @GetMapping("/logout")
    public boolean logoutUser(@RequestParam(required = true) String userKey) {
        return service.logOutUser(userKey);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUpUser(@RequestBody User userDetails) {
        return signUpService.userSignUp(userDetails) ? ResponseEntity.accepted().body("Successful")
                : ResponseEntity.badRequest().body("failure");
    }

}
