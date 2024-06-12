package com.travellers_apis.nomadic_bus.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.travellers_apis.nomadic_bus.services.UserLoginService;
import com.travellers_apis.nomadic_bus.services.UserSignUpService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    CustomSecurityFilter securityFilterChain(CustomAuthenticationManager manager) {
        return new CustomSecurityFilter(manager, new AntPathRequestMatcher("/user/login", "POST"));
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, CustomSecurityFilter filter, CustomAuthenticationManager manager,
            CustomAuthenticationProvider provider) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(
                        req -> req
                                .requestMatchers("/user/login")
                                .permitAll()
                                .requestMatchers("/user/signup")
                                .permitAll()
                                .anyRequest().authenticated())
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .authenticationManager(manager)
                .authenticationProvider(provider)
                .build();
    }

    @Bean
    CustomAuthenticationProvider authenticationProvider(PasswordEncoder encoder, CustomUserDetailsManager manager) {
        return new CustomAuthenticationProvider(encoder, manager);
    }

    @Bean
    CustomAuthenticationManager authenticationManager(CustomAuthenticationProvider provider) {
        return new CustomAuthenticationManager(provider);
    }

    @Bean
    CustomUserDetailsManager userDetailsManager(UserLoginService loginService, UserSignUpService signUpService) {
        return new CustomUserDetailsManager(loginService, signUpService);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
