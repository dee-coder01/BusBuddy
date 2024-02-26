package com.travellers_apis.nomadic_bus.models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomErrorDetails {
    private LocalDateTime time;
    private String message;
    private String details;
}
