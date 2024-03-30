package com.travellers_apis.nomadic_bus.controllers;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/user/reservation")
public class ReservationController {

    @Autowired
    private ReservationService service;

    @PostMapping("/add")
    public ReservationResponseDTO postMethodName(@Valid @RequestBody ReservationDTO dto,
            @RequestParam(required = true) String userKey)
            throws UserLoginException, RouteException, BusException, ReservationException {
        return service.addReservation(dto, userKey);
    }

}