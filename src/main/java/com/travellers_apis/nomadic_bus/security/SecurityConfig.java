package com.travellers_apis.nomadic_bus.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    CustomSecurityFilter securityFilterChain() {
        return new CustomSecurityFilter(authenticationManager(), new AntPathRequestMatcher("/user/login", "POST"));
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(
                        req -> req
                                .requestMatchers("/user/login")
                                .permitAll()
                                .requestMatchers("/user/signup")
                                .permitAll()
                                .anyRequest().authenticated())
                .addFilterBefore(securityFilterChain(), UsernamePasswordAuthenticationFilter.class)
                .authenticationManager(authenticationManager())
                .authenticationProvider(authenticationProvider())
                .build();
    }

    @Bean
    CustomAuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider();
    }

    @Bean
    AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

    @Bean
    CustomUserDetailsManager userDetailsManager() {
        return new CustomUserDetailsManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
