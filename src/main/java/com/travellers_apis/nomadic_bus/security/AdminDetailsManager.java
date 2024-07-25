package com.travellers_apis.nomadic_bus.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;

import com.travellers_apis.nomadic_bus.models.Admin;
import com.travellers_apis.nomadic_bus.services.AdminLoginService;
import com.travellers_apis.nomadic_bus.services.AdminSignUpService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AdminDetailsManager implements UserDetailsManager {
    final AdminLoginService loginService;
    final AdminSignUpService signUpService;

    @Override
    public AdminDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin user = loginService.findUserWithUserName(username);
        if (user == null)
            return null;
        return generateCustomAdminDetails(user);
    }

    @Override
    public void createUser(UserDetails user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createUser'");
    }

    @Override
    public void updateUser(UserDetails user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public void deleteUser(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
    }

    @Override
    public boolean userExists(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'userExists'");
    }

    public AdminDetails generateCustomAdminDetails(Admin Admin) {
        return AdminDetails.builder().userName(Admin.getEmail()).password(Admin.getPassword())
                .mobile(Admin.getMobile()).build();
    }

    public Admin generateAdminObj(AdminDetails customAdminDetails) {
        return Admin.builder()
                .email(customAdminDetails.getUsername())
                .password(customAdminDetails.getPassword())
                .mobile(customAdminDetails.getMobile())
                .build();
    }
}
