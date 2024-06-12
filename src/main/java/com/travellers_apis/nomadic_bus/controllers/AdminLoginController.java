package com.travellers_apis.nomadic_bus.controllers;

import com.travellers_apis.nomadic_bus.models.LoginCredential;
import com.travellers_apis.nomadic_bus.models.UserSession;
import com.travellers_apis.nomadic_bus.services.AdminLoginService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminLoginController {
    private AdminLoginService service;

    @PostMapping("/login")
    public ResponseEntity<UserSession> getMethodName(@Valid @RequestBody LoginCredential admin) {
        UserSession session = service.validateAdminCredential(admin);
        if (session != null) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(session);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(session);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logOutAdminEntity(@RequestBody UserSession session) {
        if (session == null) {
            return ResponseEntity.badRequest().body("User is not logged in.");
        }
        if (service.logOutAdmin(session)) {
            return ResponseEntity.ok().body("User logout successful.");
        } else {
            return ResponseEntity.badRequest().body("Something went wrong.");
        }
    }

}
