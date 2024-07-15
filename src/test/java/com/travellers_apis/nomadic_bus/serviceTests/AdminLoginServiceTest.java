package com.travellers_apis.nomadic_bus.serviceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.travellers_apis.nomadic_bus.commons.AdminException;
import com.travellers_apis.nomadic_bus.models.Admin;
import com.travellers_apis.nomadic_bus.models.LoginCredential;
import com.travellers_apis.nomadic_bus.models.UserSession;
import com.travellers_apis.nomadic_bus.repositories.AdminRepository;
import com.travellers_apis.nomadic_bus.services.AdminLoginService;
import com.travellers_apis.nomadic_bus.services.UserSessionService;

public class AdminLoginServiceTest {
    @InjectMocks
    private AdminLoginService loginService;
    @Mock
    private UserSessionService sessionService;

    @Mock
    private AdminRepository adminRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateAdminSession() {
        Admin admin = AdminTestUtils.createAdmin();
        when(adminRepository.findByEmailAndPassword("Admin@gmail.com", "Password@123"))
                .thenReturn(Optional.of(admin));
        UserSession session = new UserSession();
        session.setTime(LocalDateTime.now());
        session.setUserID(null);
        when(sessionService.createNewSession(new UserSession()))
                .thenReturn(new UserSession());
        LoginCredential loginCredential = AdminTestUtils.createAdminLoginCredential();
        loginService.createAdminSession(loginCredential);
    }

    @Test
    public void testFindAdminByEmailAndPassword() {
        Admin admin = AdminTestUtils.createAdmin();
        when(adminRepository.findByEmailAndPassword("Admin@gmail.com", "Password@123"))
                .thenReturn(Optional.of(admin));
        when(adminRepository.findByEmailAndPassword("Admin@gmail.com", "Password"))
                .thenThrow(new AdminException("Check your credential."));
        LoginCredential loginCredential = AdminTestUtils.createAdminLoginCredential();
        Admin dbAdmin = loginService.findAdminByEmailAndPassword(loginCredential);
        assertEquals(admin.getEmail(), dbAdmin.getEmail());
        LoginCredential basCredential = AdminTestUtils.createBadAdminLoginCredential();
        assertThrows(AdminException.class, () -> loginService.findAdminByEmailAndPassword(basCredential));
    }

    @Test
    public void testLogoutAdmin() {
        when(sessionService.deleteUserSessionByUserKey("session_key")).thenReturn(true);
        loginService.logOutAdmin("session_key");
    }
}
