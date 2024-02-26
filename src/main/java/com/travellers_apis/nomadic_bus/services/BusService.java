package com.travellers_apis.nomadic_bus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travellers_apis.nomadic_bus.commons.AdminException;
import com.travellers_apis.nomadic_bus.commons.BusException;
import com.travellers_apis.nomadic_bus.models.Bus;
import com.travellers_apis.nomadic_bus.models.Route;
import com.travellers_apis.nomadic_bus.models.UserSession;
import com.travellers_apis.nomadic_bus.repositories.BusRepository;
import com.travellers_apis.nomadic_bus.repositories.UserLoginRepo;

@Service
public class BusService {
    @Autowired
    private BusRepository busRepo;
    @Autowired
    private UserLoginRepo userLoginRepo;

    public Bus addNewBusToDB(Bus bus, String userKey) throws AdminException, BusException {
        UserSession currentSession = userLoginRepo.findByUserID(userKey);
        if (currentSession == null)
            throw new AdminException("user key is not valid, Please provide a valid key.");
        if (bus.getRouteFrom().equals(bus.getRouteTo()))
            throw new BusException("journey start and destination can't be same.");
        Route route = new Route(bus.getRouteFrom(), bus.getRouteTo(), bus.getRoute().getDistance());
        bus.setRoute(route);
        route.getBusList().add(bus);
        busRepo.save(bus);
        return bus;
    }

}
