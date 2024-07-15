package com.travellers_apis.nomadic_bus.serviceTests;

import java.time.LocalDateTime;
import java.util.UUID;

import com.travellers_apis.nomadic_bus.models.UserSession;

public class SessionTestUtils {
    // public static String UUID = "20b42208-2b98-4b1a-ba30-aed27a67e0d3";

    public static UserSession createSession() {
        UserSession session = new UserSession();
        session.setTime(LocalDateTime.now());
        session.setUuid(UUID.randomUUID().toString());
        session.setUserID(1L);
        return session;
    }

    public static UserSession createBadSession() {
        UserSession session = createSession();
        session.setUserID(null);
        return session;
    }
}
