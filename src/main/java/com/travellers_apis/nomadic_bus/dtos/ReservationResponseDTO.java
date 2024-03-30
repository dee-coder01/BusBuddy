package com.travellers_apis.nomadic_bus.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservationResponseDTO {
    private String status;
    private String source;
    private String destination;
    private LocalDate journeyDate;
    private Integer bookedSeat;
    private Integer fare;
    private String busName;
    private String driverName;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
}
