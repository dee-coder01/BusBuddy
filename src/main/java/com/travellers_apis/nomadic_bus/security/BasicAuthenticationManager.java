package com.travellers_apis.nomadic_bus.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.travellers_apis.nomadic_bus.security.authKeyAuthentication.AuthenticationKeyLoginProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BasicAuthenticationManager implements AuthenticationManager {
    private final FormLoginAuthenticationProvider provider;
    private final AuthenticationKeyLoginProvider keyLoginProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (keyLoginProvider.supports(authentication.getClass())) {
            return keyLoginProvider.authenticate(authentication);
        }
        if (provider.supports(authentication.getClass())) {
            return provider.authenticate(authentication);
        }
        throw new BadCredentialsException("Some went wrong");
    }
}
