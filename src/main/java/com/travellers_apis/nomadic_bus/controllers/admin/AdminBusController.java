package com.travellers_apis.nomadic_bus.controllers.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.travellers_apis.nomadic_bus.models.Bus;
import com.travellers_apis.nomadic_bus.services.BusService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminBusController {
    private BusService busService;

    @PostMapping("/bus")
    public ResponseEntity<Bus> addNewBus(@Valid @RequestBody Bus bus,
            @RequestParam(required = true, name = "key") String adminKey) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        System.out.println(bus);
        busService.addNewBus(bus, adminKey);
        return ResponseEntity.accepted().body(bus);
    }

    @PutMapping("/bus")
    public ResponseEntity<Bus> updateBusEntity(@Valid @RequestBody Bus bus) {
        Bus updatedBus = busService.updateBusInfo(bus);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedBus);
    }

    @DeleteMapping("/bus/{busId}")
    public ResponseEntity<Bus> deleteBusEntity(@PathVariable("busId") Integer busId) {
        Bus deletedBus = busService.deleteBusInfo(busId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(deletedBus);
    }
}
