package com.travellers_apis.nomadic_bus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.travellers_apis.nomadic_bus.commons.AdminException;
import com.travellers_apis.nomadic_bus.commons.BusException;
import com.travellers_apis.nomadic_bus.models.Bus;
import com.travellers_apis.nomadic_bus.services.BusService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/bus/")
public class BusController {
    @Autowired
    private BusService busService;

    @PostMapping("admin/add")
    public ResponseEntity<Bus> addNewBus(@Valid @RequestBody Bus bus, @RequestParam(name = "key") String userKey)
            throws AdminException, BusException {
        busService.addNewBusToDB(bus, userKey);
        return ResponseEntity.accepted().body(bus);
    }

    @PutMapping("admin/update")
    public ResponseEntity<Bus> updateBusEntity(@Valid @RequestBody Bus bus,
            @RequestParam(name = "key") String userKey) throws AdminException, BusException, RouteException {
        Bus updatedBus = busService.updateBusInfo(bus, userKey);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedBus);
    }

    @DeleteMapping("admin/delete/{busId}")
    public ResponseEntity<Bus> deleteBusEntity(@PathVariable("busId") Integer busId,
            @RequestParam(name = "key") String userKey) throws AdminException, BusException, RouteException {
        Bus deletedBus = busService.deleteBusInfo(busId, userKey);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(deletedBus);
    }
}
