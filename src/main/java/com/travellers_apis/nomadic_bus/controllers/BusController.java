package com.travellers_apis.nomadic_bus.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.travellers_apis.nomadic_bus.models.Bus;
import com.travellers_apis.nomadic_bus.services.BusService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/bus")
@AllArgsConstructor
public class BusController {
    private BusService busService;

    @GetMapping("/all")
    public ResponseEntity<List<Bus>> getAllBusInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(busService.getAllBus());
    }

    @GetMapping("/{busId}")
    public ResponseEntity<Bus> getBusInfo(@PathVariable(name = "busId") Integer busId) {
        return ResponseEntity.status(HttpStatus.OK).body(busService.getBusInfo(busId));
    }

    @GetMapping("/type/{busType}")
    public ResponseEntity<Set<Bus>> getBusesByBusTypeHandler(@PathVariable("busType") String busType) {
        Set<Bus> busList = busService.findAllBusByBusType(busType);
        return ResponseEntity.status(HttpStatus.OK).body(busList);
    }
}
