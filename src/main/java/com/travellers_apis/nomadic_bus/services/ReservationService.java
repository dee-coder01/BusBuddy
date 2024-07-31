package com.travellers_apis.nomadic_bus.services;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travellers_apis.nomadic_bus.commons.BusException;
import com.travellers_apis.nomadic_bus.commons.NoSessionFoundException;
import com.travellers_apis.nomadic_bus.commons.ReservationException;
import com.travellers_apis.nomadic_bus.commons.RouteException;
import com.travellers_apis.nomadic_bus.commons.UserLoginException;
import com.travellers_apis.nomadic_bus.dtos.ReservationDTO;
import com.travellers_apis.nomadic_bus.dtos.ReservationResponseDTO;
import com.travellers_apis.nomadic_bus.models.Bus;
import com.travellers_apis.nomadic_bus.models.Reservation;
import com.travellers_apis.nomadic_bus.models.User;
import com.travellers_apis.nomadic_bus.models.UserSession;
import com.travellers_apis.nomadic_bus.repositories.ReservationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {
    final ReservationRepository repository;
    final RouteService routeService;
    final BusService busService;
    final UserSessionService sessionService;
    final UserLoginService loginService;

    @Transactional
    public ReservationResponseDTO addReservation(ReservationDTO dto, String userKey) {
        try {
            UserSession userSession = sessionService.findSessionByUserKey(userKey).get();
            User user = loginService.findUserWithUserId(userSession.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("Something went wrong"));
            if (!routeService.isRouteAvailable(dto.getSource(), dto.getDestination()))
                throw new RouteException(
                        "Route is not available from " + dto.getSource() + " to " + dto.getDestination() + ".");
            Bus bus = busService.getBusInfo(dto.getBusId());
            int availableSeats = bus.getAvailableSeats();
            if (availableSeats == 0)
                throw new ReservationException("No seat available in the bus.");
            if (availableSeats < dto.getBookedSeat())
                throw new ReservationException("Only " + availableSeats + " seats are available");
            if (dto.getJourneyDate().isBefore(LocalDate.now()))
                throw new ReservationException("Journey Date should be in Future");
            Reservation reservation = generateReservationData(dto, bus, user);
            repository.save(reservation);
            return generateReservationResponseDTO(dto, bus, "Booked");
        } catch (NoSessionFoundException e) {
            throw new UserLoginException("User key is not correct! Please provide a valid key.");
        } catch (BusException e) {
            throw new BusException("Exception while finding the bus" + e.getMessage());
        }
    }

    @Transactional
    public ReservationResponseDTO cancelReservation(ReservationDTO dto, String userKey) {
        if (!routeService.isRouteAvailable(dto.getSource(), dto.getDestination()))
            throw new RouteException(
                    "Something went wrong, Please connect with customer support for resolution.");
        Bus bus = busService.getBusInfo(dto.getBusId());
        repository.deleteByReservationInfo(dto.getBookedSeat(), dto.getBusId(),
                dto.getJourneyDate(), dto.getSource(), dto.getDestination());
        return generateReservationResponseDTO(dto, bus, "Cancelled");
    }

    public ReservationResponseDTO generateReservationResponseDTO(ReservationDTO dto, Bus bus, String status) {
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
