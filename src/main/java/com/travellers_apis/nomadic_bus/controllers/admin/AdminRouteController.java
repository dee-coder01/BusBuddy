package com.travellers_apis.nomadic_bus.controllers.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travellers_apis.nomadic_bus.models.Route;
import com.travellers_apis.nomadic_bus.services.RouteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminRouteController {
    final RouteService routeService;

    @PostMapping("/route")
    public ResponseEntity<Route> addNewRouteEntity(@Valid @RequestBody Route route) {
        Route newRoute = routeService.addRoute(route);
        return new ResponseEntity<>(newRoute, HttpStatus.ACCEPTED);
    }
}
