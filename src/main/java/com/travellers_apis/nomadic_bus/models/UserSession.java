package com.travellers_apis.nomadic_bus.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class UserSession {
    @Id
    @Column(unique = true)
    private String userID;
    private String uuid;
    private LocalDateTime time;
}
