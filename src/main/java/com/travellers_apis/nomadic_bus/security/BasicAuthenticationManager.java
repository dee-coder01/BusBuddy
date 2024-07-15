package com.travellers_apis.nomadic_bus.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class BasicAuthenticationManager implements AuthenticationManager {
    private FormLoginAuthenticationProvider provider;

    public BasicAuthenticationManager(FormLoginAuthenticationProvider provider) {
        this.provider = provider;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!provider.supports(authentication.getClass()))
            throw new BadCredentialsException("Bad token found.");
        return provider.authenticate(authentication);
    }
}
