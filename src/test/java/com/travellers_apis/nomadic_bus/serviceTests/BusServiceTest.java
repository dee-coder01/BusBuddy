package com.travellers_apis.nomadic_bus.serviceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.travellers_apis.nomadic_bus.commons.BusException;
import com.travellers_apis.nomadic_bus.models.Bus;
import com.travellers_apis.nomadic_bus.models.Route;
import com.travellers_apis.nomadic_bus.repositories.BusRepository;
import com.travellers_apis.nomadic_bus.serviceTests.utils.BusTestUtils;
import com.travellers_apis.nomadic_bus.serviceTests.utils.RouteTestUtils;
import com.travellers_apis.nomadic_bus.services.BusService;
import com.travellers_apis.nomadic_bus.services.RouteService;
import com.travellers_apis.nomadic_bus.services.UserSessionService;

public class BusServiceTest {
    @InjectMocks
    BusService busService;
    @Mock
    BusRepository busRepository;
    @Mock
    UserSessionService sessionService;
    @Mock
    RouteService routeService;

    Route route;
    Bus bus;
    Bus invalidBus;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        bus = BusTestUtils.createValidBus();
        route = RouteTestUtils.createValidRoute();
        invalidBus = BusTestUtils.createInvalidBus();
        bus.setRoute(route);
    }

    @Test
    public void testAddNewBus_withExistingRoute() {
        when(routeService.isRouteAvailable(route.getRouteFrom(), route.getRouteTo())).thenReturn(true);
        when(busRepository.save(bus)).thenReturn(bus);
        assertEquals(busService.addNewBus(bus), bus);
    }

    @Test
    public void testAddNewBus_withNewRoute() {
        when(routeService.isRouteAvailable(route.getRouteFrom(), route.getRouteTo())).thenReturn(false);
        when(routeService.addRoute(route)).thenReturn(route);
        when(busRepository.save(bus)).thenReturn(bus);
        assertEquals(busService.addNewBus(bus), bus);
    }

    @Test
    public void testAddNewBus_withInvalidRoute() {
        BusException exception = assertThrows(BusException.class, () -> busService.addNewBus(invalidBus));
        assertEquals(exception.getMessage(), "Journey start and destination can't be same.");
    }

    @Test
    public void testUpdateBusInfo() {
        when(busRepository.findById(1)).thenReturn(Optional.of(bus));
        when(busRepository.save(bus)).thenReturn(bus);
        when(routeService.addRoute(route)).thenReturn(route);
        assertEquals(busService.updateBusInfo(bus), bus);
    }

    @Test
    public void testDeleteBusInfo() {
        Bus busWithBookedSeats = BusTestUtils.createValidBusWithBookedSeats();
        when(busRepository.findById(1)).thenReturn(Optional.of(busWithBookedSeats));
        assertThrows(BusException.class, () -> busService.deleteBusInfo(busWithBookedSeats.getBusId()));
        when(busRepository.findById(1)).thenReturn(Optional.of(bus));
        when(routeService.addRoute(route)).thenReturn(route);
        assertEquals(busService.deleteBusInfo(bus.getBusId()), bus);
    }
}