package com.travellers_apis.nomadic_bus.security.authKeyAuthentication;

import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.travellers_apis.nomadic_bus.security.UserRoles;
import com.travellers_apis.nomadic_bus.services.AdminSessionService;
import com.travellers_apis.nomadic_bus.services.UserSessionService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthenticationKeyLoginProvider implements AuthenticationProvider {
    private final Object sessionService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (sessionService instanceof AdminSessionService) {
            AdminSessionService adminSessionService = (AdminSessionService) sessionService;
            return adminSessionService
                    .findSessionByAdminKey((String) authentication.getPrincipal())
                    .map(session -> new AuthenticationKeyToken(session.getUuid(),
                            List.of(new SimpleGrantedAuthority(UserRoles.ADMIN.toString()))))
                    .get();
        } else if (sessionService instanceof UserSessionService) {
            UserSessionService userSessionService = (UserSessionService) sessionService;
            return userSessionService
                    .findSessionByUserKey((String) authentication.getPrincipal())
                    .map(session -> new AuthenticationKeyToken(session.getUuid(),
                            List.of(new SimpleGrantedAuthority(UserRoles.USER.toString()))))
                    .get();
        } else {
            throw new RuntimeException(
                    "Please provide the AuthenticationKeyLoginProvider with instance of either AdminSessionService or UserSessionService.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AuthenticationKeyToken.class.equals(authentication);
    }
}
