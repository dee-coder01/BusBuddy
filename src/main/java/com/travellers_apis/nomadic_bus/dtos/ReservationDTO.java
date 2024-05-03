package com.travellers_apis.nomadic_bus.dtos;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

// import jakarta.persistence.Entity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// @Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
    @NotNull(message = "Source required to book a reservation")
    @NotBlank(message = "Source should not be blanked")
    private String source;

    @NotNull(message = "Destination required to book a reservation")
    @NotBlank(message = "Destination should not be blanked")
    private String destination;

    // @NotNull(message = "Bus id required to book a reservation")
    // @NotBlank(message = "Bus id should not be blanked")
    private Integer busId;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate journeyDate;

    @Min(1)
    private Integer bookedSeat;
}
