package com.travellers_apis.nomadic_bus.serviceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.travellers_apis.nomadic_bus.commons.AdminException;
import com.travellers_apis.nomadic_bus.models.Admin;
import com.travellers_apis.nomadic_bus.models.AdminSession;
import com.travellers_apis.nomadic_bus.models.LoginCredential;
import com.travellers_apis.nomadic_bus.repositories.AdminRepository;
import com.travellers_apis.nomadic_bus.repositories.AdminSessionRepository;
import com.travellers_apis.nomadic_bus.services.AdminLoginService;
import com.travellers_apis.nomadic_bus.services.AdminSessionService;

public class AdminLoginServiceTest {
    @InjectMocks
    private AdminLoginService loginService;
    @Mock
    private AdminSessionService sessionService;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private AdminSessionRepository adminSessionRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateAdminSession() {
        Admin admin = AdminTestUtils.createAdmin();
        AdminSession session = SessionTestUtils.createAdminSession();
        when(adminRepository.findByEmail(admin.getEmail())).thenReturn(Optional.of(admin));
        when(sessionService
                .createNewSession(argThat(adminSession -> adminSession.getAdmin().equals(admin))))
                .thenReturn(session);
        LoginCredential loginCredential = AdminTestUtils.createAdminLoginCredential();
        assertEquals(loginService.createAdminSession(loginCredential.getEmail()).getUuid(), session.getUuid());
    }

    @Test
    public void testFindAdminByEmailAndPassword() {
        Admin admin = AdminTestUtils.createAdmin();
        when(adminRepository.findByEmailAndPassword("Admin@gmail.com", "Password@123"))
                .thenReturn(Optional.of(admin));
        when(adminRepository.findByEmailAndPassword("Admin@gmail.com", "Password"))
                .thenThrow(new AdminException("Check your credential."));
        LoginCredential loginCredential = AdminTestUtils.createAdminLoginCredential();
        Admin dbAdmin = loginService.findAdminByEmailAndPassword(loginCredential).get();
        assertEquals(admin.getEmail(), dbAdmin.getEmail());
        LoginCredential basCredential = AdminTestUtils.createBadAdminLoginCredential();
        assertThrows(AdminException.class, () -> loginService.findAdminByEmailAndPassword(basCredential));
    }

    @Test
    public void testLogoutAdmin() {
        when(sessionService.deleteAdminSessionByAdminKey("session_key")).thenReturn(true);
        when(adminSessionRepository.deleteByUuid("session_key")).thenReturn(true);
        loginService.logOutAdmin("session_key");
    }
}
