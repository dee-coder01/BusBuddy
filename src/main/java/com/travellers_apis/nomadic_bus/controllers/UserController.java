package com.travellers_apis.nomadic_bus.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travellers_apis.nomadic_bus.commons.UserException;
import com.travellers_apis.nomadic_bus.dtos.UserSessionDTO;
import com.travellers_apis.nomadic_bus.models.LoginCredential;
import com.travellers_apis.nomadic_bus.models.User;
import com.travellers_apis.nomadic_bus.services.UserLoginService;
import com.travellers_apis.nomadic_bus.services.UserSignUpService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
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
    public ResponseEntity<String> logoutUser(@RequestParam(required = true, name = "key") String userKey) {
        try {
            service.logOutUser(userKey);
            return ResponseEntity.status(HttpStatus.OK).body("Log out successful.");
        } catch (UserException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("failed to Log out, Please try again.");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUpUser(@Valid @RequestBody User userDetails) {
        return signUpService.userSignUp(userDetails) ? ResponseEntity.accepted().body("Successful")
                : ResponseEntity.badRequest().body("failure");
    }

}
