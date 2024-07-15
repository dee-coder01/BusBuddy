package com.travellers_apis.nomadic_bus.serviceTests;

import com.travellers_apis.nomadic_bus.models.Admin;
import com.travellers_apis.nomadic_bus.models.LoginCredential;

public class AdminTestUtils {
    public static Admin createAdmin() {
        Admin admin = new Admin();
        admin.setEmail("Admin@gmail.com");
        admin.setPassword("Password@123");
        return admin;
    }

    public static Admin createBadAdmin() {
        Admin admin = createAdmin();
        admin.setPassword("Password");
        return admin;
    }

    public static LoginCredential createAdminLoginCredential() {
        LoginCredential loginCredential = new LoginCredential();
        loginCredential.setEmail("Admin@gmail.com");
        loginCredential.setPassword("Password@123");
        return loginCredential;
    }

    public static LoginCredential createBadAdminLoginCredential() {
        LoginCredential loginCredential = createAdminLoginCredential();
        loginCredential.setPassword("Password");
        return loginCredential;
    }
}
