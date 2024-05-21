package com.travellers_apis.nomadic_bus.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.travellers_apis.nomadic_bus.models.LoginCredential;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    CustomUserDetailsManager userDetailsManager;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails userDetails = userDetailsManager
                .loadUserByUsername(((LoginCredential) authentication.getPrincipal()).getEmail());
        if (!passwordEncoder.matches((((LoginCredential) authentication.getPrincipal()).getPassword()),
                userDetails.getPassword()))
            throw new BadCredentialsException("Invalid password.");
        LoginCredential loginCredential = new LoginCredential(userDetails.getUsername(), userDetails.getPassword());
        System.out.println(loginCredential);
        CustomAuthenticationToken token = new CustomAuthenticationToken(loginCredential, null, List.of(() -> "Read"));
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthenticationToken.class.equals(authentication);
    }

}
