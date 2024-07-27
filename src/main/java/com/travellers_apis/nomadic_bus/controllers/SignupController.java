package com.travellers_apis.nomadic_bus.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.travellers_apis.nomadic_bus.models.Admin;
import com.travellers_apis.nomadic_bus.models.User;
import com.travellers_apis.nomadic_bus.services.AdminSignUpService;
import com.travellers_apis.nomadic_bus.services.UserSignUpService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignupController {
    final AdminSignUpService adminSignUpService;
    private UserSignUpService userSignUpService;

    @PostMapping("/admin")
    public ResponseEntity<Admin> registerAsAdmin(@Valid @RequestBody Admin admin) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminSignUpService.addNewAdmin(admin));
    }

    @PostMapping("/user")
    public ResponseEntity<String> signUpUser(@Valid @RequestBody User userDetails) {
        return userSignUpService.userSignUp(userDetails) ? ResponseEntity.accepted().body("Successful")
                : ResponseEntity.badRequest().body("failure");
    }
}
