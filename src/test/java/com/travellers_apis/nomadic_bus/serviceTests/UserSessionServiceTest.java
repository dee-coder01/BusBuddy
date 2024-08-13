package com.travellers_apis.nomadic_bus.serviceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.travellers_apis.nomadic_bus.commons.NoSessionFoundException;
import com.travellers_apis.nomadic_bus.models.UserSession;
import com.travellers_apis.nomadic_bus.repositories.UserSessionRepository;
import com.travellers_apis.nomadic_bus.serviceTests.utils.SessionTestUtils;
import com.travellers_apis.nomadic_bus.services.UserSessionService;

public class UserSessionServiceTest {
    @InjectMocks
    UserSessionService sessionService;

    @Mock
    UserSessionRepository sessionRepository;

    private UserSession session;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        session = SessionTestUtils.createUserSession();
    }

    @Test
    public void testFindSessionByUserId() {
        when(sessionRepository.findByUserId(1L)).thenReturn(Optional.of(session));
        when(sessionRepository.findByUserId(2L))
                .thenThrow(new NoSessionFoundException("Session not found for the user, Please login."));
        UserSession session2 = sessionService.findSessionByUserId(1L).get();
        assertEquals(session.getUuid(), session2.getUuid());
        assertThrows(NoSessionFoundException.class, () -> sessionService.findSessionByUserId(2L));
    }

    @Test
    public void testValidateUserKey() {
        when(sessionRepository.findByUuid(session.getUuid())).thenReturn(Optional.of(session));
        assertTrue(sessionService.validateUserKey(session.getUuid()));
    }

    @Test
    public void testFindSessionByUserKey() {
        when(sessionRepository.findByUuid(session.getUuid())).thenReturn(Optional.of(session));
        UserSession session2 = sessionService.findSessionByUserKey(session.getUuid()).get();
        assertEquals(session.getId(), session2.getId());
        when(sessionRepository.findByUuid("bad_user_key"))
                .thenThrow(new NoSessionFoundException("Invalid user key."));
        assertThrows(NoSessionFoundException.class, () -> sessionService.findSessionByUserKey("bad_user_key"));
    }

    @Test
    public void testDeleteUserSession() {
        when(sessionRepository.deleteByUserId(session.getId())).thenReturn(true);
        assertTrue(sessionService.deleteUserSession(session.getId()));
    }

    @Test
    public void testDeleteUserSessionByUserKey() {
        when(sessionRepository.deleteByUuid(session.getUuid())).thenReturn(true);
        assertTrue(sessionService.deleteUserSessionByUserKey(session.getUuid()));
    }

    @Test
    public void testCreateNewSession() {
        when(sessionRepository.save(session)).thenReturn(session);
        UserSession session2 = sessionService.createNewSession(session);
        assertEquals(session.getId(), session2.getId());
    }
}
