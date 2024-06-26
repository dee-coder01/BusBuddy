package com.travellers_apis.nomadic_bus.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.travellers_apis.nomadic_bus.commons.AdminException;
import com.travellers_apis.nomadic_bus.commons.BusException;
import com.travellers_apis.nomadic_bus.commons.RouteException;
import com.travellers_apis.nomadic_bus.models.Bus;
import com.travellers_apis.nomadic_bus.services.BusService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/bus")
@AllArgsConstructor
public class BusController {
    private BusService busService;

    @PostMapping("/admin")
    public ResponseEntity<Bus> addNewBus(@Valid @RequestBody Bus bus, @RequestParam(name = "key") String userKey)
            throws AdminException, BusException {
        busService.addNewBus(bus, userKey);
        return ResponseEntity.accepted().body(bus);
    }

    @PutMapping("/admin")
    public ResponseEntity<Bus> updateBusEntity(@Valid @RequestBody Bus bus,
            @RequestParam(name = "key") String userKey) throws AdminException, BusException, RouteException {
        Bus updatedBus = busService.updateBusInfo(bus, userKey);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedBus);
    }

    @DeleteMapping("/admin/{busId}")
    public ResponseEntity<Bus> deleteBusEntity(@PathVariable("busId") Integer busId,
            @RequestParam(name = "key") String userKey) throws AdminException, BusException, RouteException {
        Bus deletedBus = busService.deleteBusInfo(busId, userKey);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(deletedBus);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Bus>> getAllBusInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(busService.getAllBus());
    }

    @GetMapping("/{busId}")
    public ResponseEntity<Bus> getBusInfo(@PathVariable(name = "busId") Integer busId) throws BusException {
        return ResponseEntity.status(HttpStatus.OK).body(busService.getBusInfo(busId));
    }

    @GetMapping("/type/{busType}")
    public ResponseEntity<Set<Bus>> getBusesByBusTypeHandler(@PathVariable("busType") String busType)
            throws BusException {
        Set<Bus> busList = busService.findAllBusByBusType(busType);
        return ResponseEntity.status(HttpStatus.OK).body(busList);
    }
}
