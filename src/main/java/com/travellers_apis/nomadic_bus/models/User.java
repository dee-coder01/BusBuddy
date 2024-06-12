package com.travellers_apis.nomadic_bus.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userID;

    @NotNull(message = "Name cannot be null!")
    @NotBlank(message = "Name cannot be blank!")
    private String firstName;
    private String lastName;

    @NotNull(message = "Mobile number cannot be null!")
    @NotBlank(message = "Mobile number cannot be blank!")
    @Pattern(regexp = "[6789]{1}[0-9]{9}", message = "Enter valid 10 digit mobile number")
    @Size(min = 10, max = 10)
    @Column(unique = true)
    private String mobile;

    @Email
    @Column(unique = true)
    private String email;

    @NotNull(message = "Password cannot be null!")
    @NotBlank(message = "Password cannot be blank!")
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<Reservation> reservationList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private final List<Feedback> feedbackList = new ArrayList<>();
}
