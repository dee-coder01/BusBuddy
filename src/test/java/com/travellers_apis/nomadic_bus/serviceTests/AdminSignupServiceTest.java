package com.travellers_apis.nomadic_bus.serviceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.travellers_apis.nomadic_bus.models.Admin;
import com.travellers_apis.nomadic_bus.repositories.AdminRepository;
import com.travellers_apis.nomadic_bus.services.AdminSignUpService;

public class AdminSignupServiceTest {
    @InjectMocks
    private AdminSignUpService signUpService;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddNewAdmin() {
        Admin admin = AdminTestUtils.createAdmin();
        when(adminRepository.save(admin)).thenReturn(admin);
        when(passwordEncoder.encode(admin.getPassword()))
                .thenReturn("encoded password");
        assertEquals(signUpService.addNewAdmin(admin).getEmail(), admin.getEmail());
    }
}
