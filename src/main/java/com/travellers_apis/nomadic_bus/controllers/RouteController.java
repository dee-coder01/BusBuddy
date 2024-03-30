package com.travellers_apis.nomadic_bus.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.travellers_apis.nomadic_bus.commons.AdminException;
import com.travellers_apis.nomadic_bus.commons.RouteException;
import com.travellers_apis.nomadic_bus.models.Route;
import com.travellers_apis.nomadic_bus.services.RouteService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/route")
public class RouteController {
    @Autowired
    private RouteService routeService;

    @PostMapping("/admin/add")
    public ResponseEntity<Route> addNewRouteEntity(@Valid @RequestBody Route route,
            @RequestParam(required = true) String key) throws AdminException, RouteException {
        Route newRoute = routeService.addRoute(route, key);
        return new ResponseEntity<Route>(newRoute, HttpStatus.ACCEPTED);
    }

    @GetMapping("/all")
    public List<List<Route>> getAllRoute(@RequestParam String source, @RequestParam String destination) {
        return routeService.getAllRouteFromSourceToDestination(source, destination);
    }

}
