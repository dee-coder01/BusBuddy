package com.travellers_apis.nomadic_bus.services;

import java.time.LocalDate;
import java.util.Set;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travellers_apis.nomadic_bus.commons.AdminException;
import com.travellers_apis.nomadic_bus.commons.BusException;
import com.travellers_apis.nomadic_bus.models.Bus;
import com.travellers_apis.nomadic_bus.models.Route;
import com.travellers_apis.nomadic_bus.repositories.BusRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BusService {
    private static final String INVALID_USER_KEY = "User key is not valid, Please provide a valid key.";
    final BusRepository busRepository;
    final UserSessionService sessionService;
    final RouteService routeService;

    @Transactional
    public Bus addNewBus(Bus bus, String userKey) {
        boolean isValidKey = sessionService.validateUserKey(userKey);
        if (!isValidKey) {
            throw new AdminException(INVALID_USER_KEY);
        }
        if (bus.getRouteFrom().equals(bus.getRouteTo()))
            throw new BusException("Journey start and destination can't be same.");
        Route route = new Route(bus.getRouteFrom(), bus.getRouteTo(), bus.getRoute().getDistance());
        bus.setRoute(route);
        route.getBusList().add(bus);
        busRepository.save(bus);
        return bus;
    }

    @Transactional
    public Bus updateBusInfo(Bus bus, String userKey) {
        boolean isValidKey = sessionService.validateUserKey(userKey);
        if (!isValidKey) {
            throw new AdminException(INVALID_USER_KEY);
        }
        if (!(busRepository.findById(bus.getBusId()).isPresent())) {
            throw new BusException("Bus not found in the system.");
        }
        Route route = routeService.addRoute(bus.getRoute(), userKey);
        bus.setRoute(route);
        return busRepository.save(bus);
    }

    @Transactional
    public Bus deleteBusInfo(@Valid Integer busId, String userKey) {
        boolean isValidKey = sessionService.validateUserKey(userKey);
        if (!isValidKey) {
            throw new AdminException(INVALID_USER_KEY);
        }
        Bus existingBus = busRepository.findById(busId)
                .orElseThrow(() -> new BusException("Bus not found with busId: " + busId));
        if (LocalDate.now().isBefore(existingBus.getBusJourneyDate())
                && existingBus.getAvailableSeats().equals(existingBus.getSeats())) {
            throw new BusException("Can't delete scheduled and reserved bus.");
        }
        busRepository.delete(existingBus);
        return existingBus;
    }

    @Transactional(readOnly = true)
    public List<Bus> getAllBus() {
        return busRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Bus getBusInfo(Integer busId) {
        return busRepository.findById(busId).orElseThrow(() -> new BusException("Bus not found with busId: " + busId));
    }

    @Transactional
    public Set<Bus> findAllBusByBusType(String busType) {
        Set<Bus> busListType = busRepository.findByBusType(busType);
        if (busListType.isEmpty()) {
            throw new BusException("There are no buses with bus type: " + busType);
        }
        return busListType;
    }
}
