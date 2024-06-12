package com.travellers_apis.nomadic_bus.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.travellers_apis.nomadic_bus.commons.AdminException;
import com.travellers_apis.nomadic_bus.commons.BusException;
import com.travellers_apis.nomadic_bus.commons.RouteException;
import com.travellers_apis.nomadic_bus.models.Bus;
import com.travellers_apis.nomadic_bus.models.Route;
import com.travellers_apis.nomadic_bus.models.UserSession;
import com.travellers_apis.nomadic_bus.repositories.BusRepository;
import com.travellers_apis.nomadic_bus.repositories.UserLoginRepo;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BusService {
    private static final String INVALID_USER_KEY = "User key is not valid, Please provide a valid key.";
    private BusRepository busRepo;
    private UserLoginRepo userLoginRepo;
    private RouteService routeService;

    public Bus addNewBusToDB(Bus bus, String userKey) throws AdminException, BusException {
        UserSession currentSession = userLoginRepo.findByUuid(userKey);
        if (currentSession == null)
            throw new AdminException(INVALID_USER_KEY);
        if (bus.getRouteFrom().equals(bus.getRouteTo()))
            throw new BusException("Journey start and destination can't be same.");
        Route route = new Route(bus.getRouteFrom(), bus.getRouteTo(), bus.getRoute().getDistance());
        bus.setRoute(route);
        route.getBusList().add(bus);
        busRepo.save(bus);
        return bus;
    }

    public Bus updateBusInfo(@Valid Bus bus, String userKey) throws AdminException, BusException, RouteException {
        UserSession currentSession = userLoginRepo.findByUuid(userKey);
        if (currentSession == null)
            throw new AdminException(INVALID_USER_KEY);
        if (bus.getBusId() == null)
            throw new BusException("Bus id can't be null! please provide bus id.");
        if (!(busRepo.findById(bus.getBusId()).isPresent()))
            throw new BusException("Bus not found in the system.");
        Route route = routeService.getRouteFromSourceToDestination(bus.getRouteFrom(), bus.getRouteTo());
        if (route == null) {
            Route route1 = new Route(bus.getRouteFrom(), bus.getRouteTo(), bus.getRoute().getDistance());
            routeService.addRoute(route, userKey);
            bus.setRoute(route1);
            return busRepo.save(bus);
        }
        routeService.addRoute(route, userKey);
        bus.setRoute(route);
        return busRepo.save(bus);
    }

    public Bus deleteBusInfo(@Valid Integer busId, String userKey) throws AdminException, BusException {
        UserSession currentSession = userLoginRepo.findByUuid(userKey);
        if (currentSession == null)
            throw new AdminException(INVALID_USER_KEY);
        Optional<Bus> bus = busRepo.findById(busId);
        if (bus.isPresent()) {
            Bus existingBus = bus.get();
            if (LocalDate.now().isBefore(existingBus.getBusJourneyDate())
                    && existingBus.getAvailableSeats().equals(existingBus.getSeats())) {
                throw new BusException("Can't delete scheduled and reserved bus.");
            }
            busRepo.delete(existingBus);
            return existingBus;
        } else
            throw new BusException("Bus not found with busId: " + busId);
    }

    public List<Bus> getAllBus() {
        return busRepo.findAll();
    }

    public Bus getBusInfo(Integer busId) throws BusException {
        Optional<Bus> busInfo = busRepo.findById(busId);
        if (busInfo.isPresent())
            return busInfo.get();
        else
            throw new BusException("Bus not found with busId: " + busId);
    }

    public List<Bus> findAllBusByBusType(String busType) throws BusException {
        List<Bus> busListType = busRepo.findByBusType(busType);
        if (busListType.isEmpty()) {
            throw new BusException("There are no buses with bus type: " + busType);
        }
        return busListType;
    }

}
