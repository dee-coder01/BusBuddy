package com.travellers_apis.nomadic_bus.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.travellers_apis.nomadic_bus.security.authKeyAuthentication.AuthenticationKeyLoginFilter;
import com.travellers_apis.nomadic_bus.security.authKeyAuthentication.AuthenticationKeyLoginProvider;
import com.travellers_apis.nomadic_bus.services.AdminLoginService;
import com.travellers_apis.nomadic_bus.services.AdminSessionService;
import com.travellers_apis.nomadic_bus.services.AdminSignUpService;
import com.travellers_apis.nomadic_bus.services.UserLoginService;
import com.travellers_apis.nomadic_bus.services.UserSessionService;
import com.travellers_apis.nomadic_bus.services.UserSignUpService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${security.public-urls}")
    private String[] publicUrls;
    @Value("${security.admin-urls}")
    private String[] adminUrls;
    @Value("${security.user-urls}")
    private String[] userUrls;

    @Bean
    BasicLoginFilter securityFilterChain(@Qualifier("AdminLoginManager") BasicAuthenticationManager manager) {
        return new BasicLoginFilter(manager, new AntPathRequestMatcher("/*/login",
                "POST"));
    }

    @Bean(name = "AdminLoginFilter")
    AuthenticationKeyLoginFilter adminKeyLoginFilterChain(
            @Qualifier("AdminLoginManager") BasicAuthenticationManager manager) {
        return new AuthenticationKeyLoginFilter(manager, new AntPathRequestMatcher("/admin/*", "POST"));
    }

    @Bean(name = "UserLoginFilter")
    AuthenticationKeyLoginFilter userKeyLoginFilterChain(
            @Qualifier("UserLoginManager") BasicAuthenticationManager manager) {
        return new AuthenticationKeyLoginFilter(manager, new AntPathRequestMatcher("/user/*", "POST"));
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, BasicLoginFilter filter,
            @Qualifier("AdminLoginFilter") AuthenticationKeyLoginFilter adminKeyLoginFilter,
            @Qualifier("UserLoginFilter") AuthenticationKeyLoginFilter userKeyLoginFilter) throws Exception {
        System.out.println(Arrays.toString(publicUrls));
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(
                        req -> req
                                .requestMatchers(publicUrls)
                                .permitAll()
                                .requestMatchers(adminUrls)
                                .hasAuthority(UserRoles.ADMIN.toString())
                                .requestMatchers(userUrls)
                                .hasAuthority(UserRoles.USER.toString())
                                .anyRequest().authenticated())
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(adminKeyLoginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(userKeyLoginFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    FormLoginAuthenticationProvider authenticationProvider(PasswordEncoder encoder,
            CustomUserDetailsManager userDetailsManager, AdminDetailsManager adminDetailsManager) {
        return new FormLoginAuthenticationProvider(userDetailsManager, adminDetailsManager, encoder);
    }

    @Bean(name = "AdminLoginProvider")
    AuthenticationKeyLoginProvider adminKeyLoginProvider(AdminSessionService sessionService) {
        return new AuthenticationKeyLoginProvider(sessionService);
    }

    @Bean(name = "UserLoginProvider")
    AuthenticationKeyLoginProvider userKeyLoginProvider(UserSessionService sessionService) {
        return new AuthenticationKeyLoginProvider(sessionService);
    }

    @Bean
    AdminDetailsManager adminDetailsManager(AdminLoginService loginService, AdminSignUpService signUpService) {
        return new AdminDetailsManager(loginService, signUpService);
    }

    @Bean(name = "AdminLoginManager")
    BasicAuthenticationManager adminAuthenticationManager(FormLoginAuthenticationProvider provider,
            @Qualifier("AdminLoginProvider") AuthenticationKeyLoginProvider keyLoginProvider) {
        return new BasicAuthenticationManager(provider, keyLoginProvider);
    }

    @Primary
    @Bean(name = "UserLoginManager")
    BasicAuthenticationManager userAuthenticationManager(FormLoginAuthenticationProvider provider,
            @Qualifier("UserLoginProvider") AuthenticationKeyLoginProvider keyLoginProvider) {
        return new BasicAuthenticationManager(provider, keyLoginProvider);
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
