package com.travellers_apis.nomadic_bus.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Email
    @NotNull(message = "Email can't be null.")
    @Column(unique = true)
    private String email;

    @NotNull(message = "Password can't be null")
    @Pattern(regexp = "[A-Za-z0-9!@#$%^&*_]{8,15}", message = "Password must Contain 'A-Z', 'a-z', '0-9' and one of these'!@#$%^&*_'.")
    private String password;
    @NotNull(message = "Phone number can't be null.")
    @NotBlank(message = "Phone number can't be blank.")
    @Size(min = 10, max = 10, message = "Phone number should have 10 digits")
    // @Pattern(regexp = "^([0-9]{10})$", message = "Phone number should have 10
    // digits")
    private String phoneNumber;
}
