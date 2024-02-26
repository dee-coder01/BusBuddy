package com.travellers_apis.nomadic_bus.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginCredential {
    @Email
    private String email;
    @NotNull(message = "Password can't be null.")
    @NotBlank(message = "Password can't be blank.")
    @Pattern(regexp = "[A-Za-z0-9@#$%^&*_]{8,15}", message = "Please check your password.")
    private String password;
}
