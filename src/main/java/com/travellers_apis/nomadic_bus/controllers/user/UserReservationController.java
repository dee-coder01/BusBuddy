package com.travellers_apis.nomadic_bus.controllers.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.travellers_apis.nomadic_bus.commons.BusException;
import com.travellers_apis.nomadic_bus.commons.ReservationException;
import com.travellers_apis.nomadic_bus.commons.RouteException;
import com.travellers_apis.nomadic_bus.commons.UserLoginException;
import com.travellers_apis.nomadic_bus.dtos.ReservationDTO;
import com.travellers_apis.nomadic_bus.dtos.ReservationResponseDTO;
import com.travellers_apis.nomadic_bus.services.ReservationService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserReservationController {

    private ReservationService service;

    @PostMapping("/reservation")
    public ReservationResponseDTO postMethodName(@Valid @RequestBody ReservationDTO dto,
            @RequestParam(required = true, name = "key") String userKey)
            throws UserLoginException, RouteException, BusException, ReservationException {
        return service.addReservation(dto, userKey);
    }

    @DeleteMapping("/reservation")
    public ReservationResponseDTO cancelReservation(@Valid @RequestBody ReservationDTO dto,
            @RequestParam(required = true, name = "key") String userKey)
            throws UserLoginException, RouteException, BusException {
        return service.cancelReservation(dto, userKey);
    }
}
