package com.travellers_apis.nomadic_bus.security.authKeyAuthentication;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class AuthenticationKeyToken extends AbstractAuthenticationToken {
    Object principle;
    Object credentials;

    public AuthenticationKeyToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    public AuthenticationKeyToken(Object principle, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principle = principle;
    }

    @Override
    public Object getPrincipal() {
        return this.principle;
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

}
