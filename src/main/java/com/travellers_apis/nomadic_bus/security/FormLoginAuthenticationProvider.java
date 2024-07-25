package com.travellers_apis.nomadic_bus.security;

import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FormLoginAuthenticationProvider implements AuthenticationProvider {

    final CustomUserDetailsManager userDetailsManager;
    final AdminDetailsManager adminDetailsManager;
    final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority(UserRoles.USER.toString()))) {
            UserDetails userDetails = userDetailsManager
                    .loadUserByUsername((String) authentication.getPrincipal());
            if (userDetails == null)
                throw new BadCredentialsException("User not found in the system.");
            if (!passwordEncoder.matches(((String) authentication.getCredentials()),
                    userDetails.getPassword()))
                throw new BadCredentialsException("Invalid password.");
            return new CustomAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(),
                    List.of(new SimpleGrantedAuthority(UserRoles.USER.toString())));
        } else {
            AdminDetails userDetails = adminDetailsManager
                    .loadUserByUsername((String) authentication.getPrincipal());
            if (userDetails == null)
                throw new BadCredentialsException("User not found in the system.");
            if (!passwordEncoder.matches(((String) authentication.getCredentials()),
                    userDetails.getPassword()))
                throw new BadCredentialsException("Invalid password.");
            return new CustomAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(),
                    List.of(new SimpleGrantedAuthority(UserRoles.ADMIN.toString())));
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthenticationToken.class.equals(authentication);
    }

}
