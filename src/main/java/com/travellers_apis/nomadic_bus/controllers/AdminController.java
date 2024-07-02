package com.travellers_apis.nomadic_bus.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.travellers_apis.nomadic_bus.models.Admin;
import com.travellers_apis.nomadic_bus.services.AdminSignUpService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private AdminSignUpService service;

    public AdminController(AdminSignUpService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<Admin> registerAsAdmin(@Valid @RequestBody Admin admin) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addNewAdmin(admin));
    }

}
