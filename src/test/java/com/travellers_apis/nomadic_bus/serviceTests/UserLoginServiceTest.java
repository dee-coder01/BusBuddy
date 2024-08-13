package com.travellers_apis.nomadic_bus.serviceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.travellers_apis.nomadic_bus.commons.UserException;
import com.travellers_apis.nomadic_bus.models.LoginCredential;
import com.travellers_apis.nomadic_bus.models.User;
import com.travellers_apis.nomadic_bus.models.UserSession;
import com.travellers_apis.nomadic_bus.repositories.UserRepository;
import com.travellers_apis.nomadic_bus.serviceTests.utils.SessionTestUtils;
import com.travellers_apis.nomadic_bus.serviceTests.utils.UserTestUtils;
import com.travellers_apis.nomadic_bus.services.UserLoginService;
import com.travellers_apis.nomadic_bus.services.UserSessionService;

public class UserLoginServiceTest {
    @InjectMocks
    UserLoginService loginService;
    @Mock
    UserRepository userRepository;
    @Mock
    UserSessionService sessionService;

    private User user;
    private UserSession session;
    private LoginCredential loginCredential;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        user = UserTestUtils.createUser();
        session = SessionTestUtils.createUserSession();
        loginCredential = UserTestUtils.createUserLoginCredential();
        session.setUser(user);
    }

    @Test
    public void testUserExistsWithUserId() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        assertTrue(loginService.userExistsWithUserId(user.getId()));
    }

    @Test
    public void testFindUserWithUserId() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        User u = loginService.findUserWithUserId(user.getId()).get();
        assertEquals(u.getEmail(), user.getEmail());
    }

    @Test
    public void testUserExistsWithUserName() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        assertTrue(loginService.userExistsWithUserName(user.getEmail()));
    }

    @Test
    public void testFindUserWithUserName() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        User u = loginService.findUserWithUserName(user.getEmail()).get();
        assertEquals(u.getEmail(), user.getEmail());
        user = UserTestUtils.createBadUser();
        when(userRepository.findByEmail(user.getEmail()))
                .thenThrow(new UserException("User not found with username: "));
        assertThrows(UserException.class, () -> loginService.findUserWithUserName(user.getEmail()));
    }

    @Test
    public void testLogOutUser() {
        when(sessionService.findSessionByUserKey(session.getUuid())).thenReturn(Optional.of(session));
        when(sessionService.deleteUserSession(session.getUser().getId())).thenReturn(true);
        UserSession currentSession = sessionService.findSessionByUserKey(session.getUuid()).get();
        loginService.logOutUser(currentSession.getUuid());
    }

    @Test
    public void testValidateUserCredential() {
        when(userRepository.findByEmail(loginCredential.getEmail()))
                .thenReturn(Optional.of(user));
        assertTrue(userRepository.findByEmail(loginCredential.getEmail())
                .isPresent());
        when(sessionService.createNewSession(
                argThat(userSession -> userSession.getUser().equals(user))))
                .thenReturn(session);
        assertEquals(loginService.validateUserCredential(loginCredential.getEmail()),
                session.getUuid());
    }
}
