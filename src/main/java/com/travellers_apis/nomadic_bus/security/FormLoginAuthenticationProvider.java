package com.travellers_apis.nomadic_bus.security;

import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

public class FormLoginAuthenticationProvider implements AuthenticationProvider {

    CustomUserDetailsManager userDetailsManager;
    PasswordEncoder passwordEncoder;

    public FormLoginAuthenticationProvider(PasswordEncoder passwordEncoder,
            CustomUserDetailsManager userDetailsManager) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsManager = userDetailsManager;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails userDetails = userDetailsManager
                .loadUserByUsername((String) authentication.getPrincipal());
        if (!passwordEncoder.matches(((String) authentication.getCredentials()),
                userDetails.getPassword()))
            throw new BadCredentialsException("Invalid password.");
        return new CustomAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(),
                List.of(() -> "Read"));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthenticationToken.class.equals(authentication);
    }

}
