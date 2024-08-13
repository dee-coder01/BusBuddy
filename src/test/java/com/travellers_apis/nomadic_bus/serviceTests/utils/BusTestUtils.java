package com.travellers_apis.nomadic_bus.serviceTests.utils;

import java.time.LocalDate;
import java.time.LocalTime;

import com.travellers_apis.nomadic_bus.models.Bus;

public class BusTestUtils {
    public static Bus createValidBus() {
        Bus bus = new Bus();
        bus.setBusId(1);
        bus.setBusName("Bus_Name");
        bus.setArrivalTime(LocalTime.of(12, 30, 0));
        bus.setDepartureTime(LocalTime.of(5, 0, 0));
        bus.setAvailableSeats(50);
        bus.setFare(500);
        bus.setBusType("Non-AC");
        bus.setBusJourneyDate(LocalDate.now().plusDays(1));
        bus.setDriverName("Driver");
        bus.setRouteFrom("Location_A");
        bus.setRouteTo("Location_B");
        bus.setSeats(50);
        return bus;
    }

    public static Bus createInvalidBus() {
        Bus bus = new Bus();
        bus.setBusId(1);
        bus.setBusName("Bus_Name");
        bus.setArrivalTime(LocalTime.of(12, 30, 0));
        bus.setDepartureTime(LocalTime.of(5, 0, 0));
        bus.setAvailableSeats(30);
        bus.setFare(500);
        bus.setBusType("Non-AC");
        bus.setBusJourneyDate(LocalDate.now().plusDays(1));
        bus.setDriverName("Driver");
        bus.setRouteFrom("Location_A");
        bus.setRouteTo(bus.getRouteFrom());
        bus.setSeats(50);
        return bus;
    }

    public static Bus createValidBusWithBookedSeats() {
        Bus bus = new Bus();
        bus.setBusId(1);
        bus.setBusName("Bus_Name");
        bus.setArrivalTime(LocalTime.of(12, 30, 0));
        bus.setDepartureTime(LocalTime.of(5, 0, 0));
        bus.setAvailableSeats(30);
        bus.setFare(500);
        bus.setBusType("Non-AC");
        bus.setBusJourneyDate(LocalDate.now().plusDays(1));
        bus.setDriverName("Driver");
        bus.setRouteFrom("Location_A");
        bus.setRouteTo(bus.getRouteFrom());
        bus.setSeats(50);
        return bus;
    }
}
