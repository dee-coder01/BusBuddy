package com.travellers_apis.nomadic_bus.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travellers_apis.nomadic_bus.models.Bus;

@Repository
public interface BusRepository extends JpaRepository<Bus, Integer> {

    Set<Bus> findByBusType(String busType);

}
