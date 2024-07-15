package com.travellers_apis.nomadic_bus.serviceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.travellers_apis.nomadic_bus.models.Admin;
import com.travellers_apis.nomadic_bus.repositories.AdminRepository;
import com.travellers_apis.nomadic_bus.services.AdminSignUpService;

public class AdminSignupServiceTest {
    @InjectMocks
    private AdminSignUpService signUpService;

    @Mock
    private AdminRepository adminRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddNewAdmin() {
        when(adminRepository.save(AdminTestUtils.createAdmin())).thenReturn(AdminTestUtils.createAdmin());
        Admin ad = signUpService.addNewAdmin(AdminTestUtils.createAdmin());
        assertEquals(ad.getEmail(), AdminTestUtils.createAdmin().getEmail());
    }
}
