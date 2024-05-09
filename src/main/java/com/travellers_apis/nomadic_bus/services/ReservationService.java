package com.travellers_apis.nomadic_bus.services;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travellers_apis.nomadic_bus.commons.BusException;
import com.travellers_apis.nomadic_bus.commons.ReservationException;
import com.travellers_apis.nomadic_bus.commons.RouteException;
import com.travellers_apis.nomadic_bus.commons.UserLoginException;
import com.travellers_apis.nomadic_bus.dtos.ReservationDTO;
import com.travellers_apis.nomadic_bus.dtos.ReservationResponseDTO;
import com.travellers_apis.nomadic_bus.models.Bus;
import com.travellers_apis.nomadic_bus.models.Reservation;
import com.travellers_apis.nomadic_bus.models.User;
import com.travellers_apis.nomadic_bus.models.UserSession;
import com.travellers_apis.nomadic_bus.repositories.ReservationRepo;
import com.travellers_apis.nomadic_bus.repositories.UserLoginRepo;
import com.travellers_apis.nomadic_bus.repositories.UserRepo;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepo repository;
    @Autowired
    private RouteService routeService;
    @Autowired
    private BusService busService;
    @Autowired
    UserRepo userRepo;
    @Autowired
    UserLoginRepo loginRepo;

    public ReservationResponseDTO addReservation(ReservationDTO dto, String userKey)
            throws UserLoginException, RouteException, BusException, ReservationException {
        UserSession userSession = loginRepo.findByUuid(userKey);
        User user = userRepo.findByUserID(userSession.getUserID());
        if (user == null)
            throw new UserLoginException("User key: " + userKey + " is invalid. Please provide a valid user key.");
        if (!routeService.isRouteAvailable(dto.getSource(), dto.getDestination()))
            throw new RouteException(
                    "Route is not available from " + dto.getSource() + " to " + dto.getDestination() + ".");
        Bus bus = busService.getBusInfo(dto.getBusId());
        int availableSeats = bus.getAvailableSeats();
        if (availableSeats < dto.getBookedSeat())
            throw new ReservationException("Only " + availableSeats + " seats are available");
        if (dto.getJourneyDate().isBefore(LocalDate.now()))
            throw new ReservationException("Journey Date should be in Future");
        Reservation reservation = generateReservationData(dto, bus, user);
        repository.save(reservation);
        return generateReservationResponseDTO(dto, bus, "Booked");
    }

    public ReservationResponseDTO cancelReservation(ReservationDTO dto, String userKey)
            throws UserLoginException, BusException {
        UserSession userSession = loginRepo.findByUuid(userKey);
        User user = userRepo.findByUserID(userSession.getUserID());
        if (user == null)
            throw new UserLoginException("User key: " + userKey + " is invalid. Please provide a valid user key.");
        if (!routeService.isRouteAvailable(dto.getSource(), dto.getDestination()))
            throw new RouteException(
                    "Something went wrong, Please connect with customer support for resolution.");
        Bus bus = busService.getBusInfo(dto.getBusId());
        repository.deleteByReservationInfo(dto.getBookedSeat(), dto.getBusId(),
                dto.getJourneyDate(), dto.getSource(), dto.getDestination());
        return generateReservationResponseDTO(dto, bus, "Cancelled");
    }

    public ReservationResponseDTO generateReservationResponseDTO(ReservationDTO dto, Bus bus, String status)
            throws BusException {
        return ReservationResponseDTO.builder()
                .status(status)
                .source(dto.getSource())
                .destination(dto.getDestination())
                .journeyDate(bus.getBusJourneyDate())
                .bookedSeat(dto.getBookedSeat())
                .fare(bus.getFare())
                .busName(bus.getBusName())
                .driverName(bus.getDriverName())
                .arrivalTime(bus.getArrivalTime())
                .departureTime(bus.getDepartureTime())
                .build();
    }

    public Reservation generateReservationData(ReservationDTO dto, Bus bus, User user) {
        return Reservation.builder()
                .source(dto.getSource())
                .destination(dto.getDestination())
                .journeyDate(dto.getJourneyDate())
                .status("Successful")
                .date(LocalDate.now())
                .time(LocalTime.now())
                .journeyDate(dto.getJourneyDate())
                .bus(bus)
                .fare(bus.getFare() * dto.getBookedSeat())
                .bookedSeat(dto.getBookedSeat())
                .user(user)
                .build();
    }
}
