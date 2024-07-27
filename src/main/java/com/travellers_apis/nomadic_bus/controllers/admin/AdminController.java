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

}
