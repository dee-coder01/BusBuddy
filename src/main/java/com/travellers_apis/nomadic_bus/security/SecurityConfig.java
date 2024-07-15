package com.travellers_apis.nomadic_bus.security;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${security.public-urls}")
    private String[] publicUrls;

    @Bean
    BasicLoginFilter securityFilterChain(BasicAuthenticationManager manager) {
        return new BasicLoginFilter(manager, new AntPathRequestMatcher("/user/login", "POST"));
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, BasicLoginFilter filter, BasicAuthenticationManager manager,
            FormLoginAuthenticationProvider provider) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(
                        req -> req
                                .requestMatchers(publicUrls)
                                .permitAll()
                                .anyRequest().authenticated())
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .authenticationManager(manager)
                .authenticationProvider(provider)
                .build();
    }

    @Bean
    FormLoginAuthenticationProvider authenticationProvider(PasswordEncoder encoder, CustomUserDetailsManager manager) {
        return new FormLoginAuthenticationProvider(encoder, manager);
    }

    @Bean
    BasicAuthenticationManager authenticationManager(FormLoginAuthenticationProvider provider) {
        return new BasicAuthenticationManager(provider);
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
