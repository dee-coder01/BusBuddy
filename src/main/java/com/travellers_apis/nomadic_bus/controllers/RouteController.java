package com.travellers_apis.nomadic_bus.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.travellers_apis.nomadic_bus.models.Route;
import com.travellers_apis.nomadic_bus.services.RouteService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@AllArgsConstructor
@RequestMapping("/route")
public class RouteController {
    private RouteService routeService;

    @PostMapping("/admin")
    public ResponseEntity<Route> addNewRouteEntity(@Valid @RequestBody Route route,
            @RequestParam(required = true, name = "key") String key) {
        Route newRoute = routeService.addRoute(route, key);
        return new ResponseEntity<>(newRoute, HttpStatus.ACCEPTED);
    }

    @GetMapping("/all")
    public List<List<Route>> getAllRoute(@RequestParam String source, @RequestParam String destination) {
        return routeService.getAllRouteFromSourceToDestination(source, destination);
    }

}
