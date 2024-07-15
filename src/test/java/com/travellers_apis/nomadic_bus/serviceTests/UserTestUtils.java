package com.travellers_apis.nomadic_bus.serviceTests;

import com.travellers_apis.nomadic_bus.models.LoginCredential;
import com.travellers_apis.nomadic_bus.models.User;

public class UserTestUtils {
    public static LoginCredential createUserLoginCredential() {
        LoginCredential loginCredential = new LoginCredential();
        loginCredential.setEmail("User@gmail.com");
        loginCredential.setPassword("Password@123");
        return loginCredential;
    }

    public static LoginCredential createBadUserLoginCredential() {
        LoginCredential loginCredential = createUserLoginCredential();
        loginCredential.setPassword("Password");
        return loginCredential;
    }

    public static User createUser() {
        User user = new User();
        user.setEmail("User@gmail.com");
        user.setPassword("Password@123");
        return user;
    }

    public static User createBadUser() {
        User user = createUser();
        user.setEmail("BadUser@gmail.com");
        user.setPassword("Password");
        return user;
    }
}
