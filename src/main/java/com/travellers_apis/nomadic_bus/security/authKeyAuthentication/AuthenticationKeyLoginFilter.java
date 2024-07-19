package com.travellers_apis.nomadic_bus.security.authKeyAuthentication;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.travellers_apis.nomadic_bus.security.BasicAuthenticationManager;

import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthenticationKeyLoginFilter extends OncePerRequestFilter {
    final BasicAuthenticationManager authenticationManager;
    final RequestMatcher requestMatcher;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        if (requestMatcher.matches(request) && SecurityContextHolder.getContext().getAuthentication() == null) {
            String authenticationKey = request.getParameter("key");
            AuthenticationKeyToken authenticationToken = new AuthenticationKeyToken(authenticationKey, null);
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            authentication.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("AuthenticationKeyLoginFilter: Authentication set: "
                    + SecurityContextHolder.getContext().getAuthentication());
        }
        filterChain.doFilter(request, response);
    }

}
