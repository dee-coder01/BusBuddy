package com.travellers_apis.nomadic_bus.security;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travellers_apis.nomadic_bus.commons.UserLoginException;
import com.travellers_apis.nomadic_bus.models.LoginCredential;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BasicLoginFilter implements Filter {

    private final BasicAuthenticationManager manager;
    private final RequestMatcher requestMatcher;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        if (requestMatcher.matches(req) && SecurityContextHolder.getContext().getAuthentication() == null) {
            ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(req);
            try {
                LoginCredential loginCredential = new ObjectMapper().readValue(wrappedRequest.getInputStream(),
                        LoginCredential.class);

                CustomAuthenticationToken userToken = new CustomAuthenticationToken(loginCredential.getEmail(),
                        loginCredential.getPassword());
                Authentication userAuthObj = manager.authenticate(userToken);
                if (!userAuthObj.isAuthenticated())
                    throw new UserLoginException("Failed to authenticate the user.");

                SecurityContextHolder.getContext().setAuthentication(userAuthObj);
            } catch (AuthenticationException e) {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.setContentType("application/json");
                PrintWriter writer = res.getWriter();
                writer.write("{\"error\": \"Invalid login request\"}");
                writer.flush();
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
