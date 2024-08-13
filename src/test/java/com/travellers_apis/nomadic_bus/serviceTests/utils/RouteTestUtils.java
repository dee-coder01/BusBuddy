package com.travellers_apis.nomadic_bus.serviceTests.utils;

import com.travellers_apis.nomadic_bus.models.Route;

public class RouteTestUtils {
    private static String locationA = "Location A", locationB = "Location B";

    public static Route createValidRoute() {
        return new Route(locationA, locationB, 100);
    }

    public static Route createInvalidRoute() {
        return new Route(locationA, locationA, 100);
    }
}
