package com.travellers_apis.nomadic_bus.controllers.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travellers_apis.nomadic_bus.commons.UserException;
import com.travellers_apis.nomadic_bus.models.UserSession;
import com.travellers_apis.nomadic_bus.services.UserLoginService;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private UserLoginService service;

    @PostMapping("/login")
    public ResponseEntity<String> userLogin() {
        UserSession userSession = (UserSession) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.accepted().body(service.validateUserCredential(userSession.getUuid()));
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logoutUser() {
        try {
            UserSession userSession = (UserSession) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            service.logOutUser(userSession.getUuid());
            return ResponseEntity.status(HttpStatus.OK).body("Log out successful.");
        } catch (UserException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("failed to Log out, Please try again.");
        }
    }

}
