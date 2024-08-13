package com.travellers_apis.nomadic_bus.controllers.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.travellers_apis.nomadic_bus.models.AdminSession;
import com.travellers_apis.nomadic_bus.services.AdminLoginService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    final AdminLoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<AdminSession> adminLogin() {
        String userName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AdminSession session = loginService.createAdminSession(userName);
        return ResponseEntity.status(HttpStatus.CREATED).body(session);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logOutAdminEntity() {
        System.out.println("Got request in logout controller");
        AdminSession adminSession = (AdminSession) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        loginService.logOutAdmin(adminSession.getUuid());
        return ResponseEntity.accepted().body("User logout successful.");
    }

}
