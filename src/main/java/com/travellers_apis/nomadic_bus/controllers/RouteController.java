package com.travellers_apis.nomadic_bus.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.travellers_apis.nomadic_bus.models.Route;
import com.travellers_apis.nomadic_bus.services.RouteService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/route")
public class RouteController {
    final RouteService routeService;

    @GetMapping("/all")
    public List<List<Route>> getAllRoute(@RequestParam String source, @RequestParam String destination) {
        return routeService.getAllRouteFromSourceToDestination(source, destination);
    }

}
