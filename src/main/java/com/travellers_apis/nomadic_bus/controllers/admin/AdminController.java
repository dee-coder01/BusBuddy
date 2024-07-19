package com.travellers_apis.nomadic_bus.controllers.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.travellers_apis.nomadic_bus.models.Admin;
import com.travellers_apis.nomadic_bus.models.AdminSession;
import com.travellers_apis.nomadic_bus.services.AdminLoginService;
import com.travellers_apis.nomadic_bus.services.AdminSignUpService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    final AdminSignUpService service;
    final AdminLoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<AdminSession> getMethodName() {
        String userName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AdminSession session = loginService.createAdminSession(userName);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(session);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logOutAdminEntity(@RequestParam(name = "key") String key) {
        if (key == null) {
            return ResponseEntity.ok().body("User is not logged in.");
        }
        loginService.logOutAdmin(key);
        return ResponseEntity.accepted().body("User logout successful.");
    }

    @PostMapping("/signup")
    public ResponseEntity<Admin> registerAsAdmin(@Valid @RequestBody Admin admin) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addNewAdmin(admin));
    }

}
