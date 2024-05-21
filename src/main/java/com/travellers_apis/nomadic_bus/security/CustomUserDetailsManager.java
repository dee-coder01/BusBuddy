package com.travellers_apis.nomadic_bus.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;

import com.travellers_apis.nomadic_bus.models.User;
import com.travellers_apis.nomadic_bus.services.UserLoginService;
import com.travellers_apis.nomadic_bus.services.UserSignUpService;

public class CustomUserDetailsManager implements UserDetailsManager {

    @Autowired
    UserLoginService loginService;
    UserSignUpService signUpService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = loginService.findUserWithUserName(username);
        CustomUserDetails userDetails = generateCustomUserDetails(user);
        return userDetails;
    }

    @Override
    public void createUser(UserDetails user) {
        if (!(user instanceof CustomUserDetails))
            throw new IllegalArgumentException("Please provide correct user UserDetailsManagerls");
        try {
            userExists(user.getUsername());
            throw new IllegalArgumentException("User with user name already exists in the system.");
        } catch (UsernameNotFoundException e) {
            CustomUserDetails customUserDetails = (CustomUserDetails) user;
            User u = generateUserObj(customUserDetails);
            signUpService.userSignUp(u);
        }
    }

    @Override
    public void updateUser(UserDetails user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public void deleteUser(String username) {
        try {
            UserDetails user = loadUserByUsername(username);
            CustomUserDetails customUserDetails = (CustomUserDetails) user;
            if (user != null) {
                User u = generateUserObj(customUserDetails);
                boolean deleted = signUpService.deleteUser(u);
                if (!deleted)
                    throw new RuntimeException("Something went wrong");
            }
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException("Bad credentials.");
        }
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
    }

    @Override
    public boolean userExists(String username) {
        try {
            loadUserByUsername(username);
            return true;
        } catch (UsernameNotFoundException e) {
            return false;
        }
    }

    public CustomUserDetails generateCustomUserDetails(User user) {
        return CustomUserDetails.builder().userName(user.getEmail()).password(user.getPassword())
                .firstName(user.getFirstName()).lastName(user.getLastName())
                .mobile(user.getMobile()).build();
    }

    public User generateUserObj(CustomUserDetails customUserDetails) {
        return User.builder()
                .email(customUserDetails.getUsername())
                .password(customUserDetails.getPassword())
                .firstName(customUserDetails.getFirstName())
                .lastName(customUserDetails.getLastName())
                .mobile(customUserDetails.getMobile())
                .build();
    }

}
