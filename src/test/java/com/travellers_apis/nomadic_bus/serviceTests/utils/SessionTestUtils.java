package com.travellers_apis.nomadic_bus.serviceTests.utils;

import java.time.LocalDateTime;
import java.util.UUID;

import com.travellers_apis.nomadic_bus.models.AdminSession;
import com.travellers_apis.nomadic_bus.models.UserSession;

public class SessionTestUtils {
    // public static String UUID = "20b42208-2b98-4b1a-ba30-aed27a67e0d3";

    public static UserSession createUserSession() {
        UserSession session = new UserSession();
        session.setTime(LocalDateTime.now());
        session.setUuid(UUID.randomUUID().toString());
        session.setId(1L);
        return session;
    }

    public static UserSession createBadUserSession() {
        UserSession session = createUserSession();
        session.setId(null);
        return session;
    }

    public static AdminSession createAdminSession() {
        AdminSession session = new AdminSession();
        session.setTime(LocalDateTime.now());
        session.setUuid(UUID.randomUUID().toString());
        session.setId(1L);
        return session;
    }

    public static AdminSession createBadAdminSession() {
        AdminSession session = createAdminSession();
        session.setId(null);
        return session;
    }
}
