package com.travellers_apis.nomadic_bus.controllers;

import com.travellers_apis.nomadic_bus.models.LoginCredential;
import com.travellers_apis.nomadic_bus.models.UserSession;
import com.travellers_apis.nomadic_bus.services.AdminLoginService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminLoginController {
    final AdminLoginService service;

    @PostMapping("/login")
    public ResponseEntity<UserSession> getMethodName(@Valid @RequestBody LoginCredential admin) {
        UserSession session = service.validateAdminCredential(admin);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(session);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logOutAdminEntity(@RequestParam(name = "key") String key) {
        if (key == null) {
            return ResponseEntity.ok().body("User is not logged in.");
        }
        service.logOutAdmin(key);
        return ResponseEntity.accepted().body("User logout successful.");
    }

}
