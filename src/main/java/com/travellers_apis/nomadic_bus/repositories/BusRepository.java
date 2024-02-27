package com.travellers_apis.nomadic_bus.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travellers_apis.nomadic_bus.models.Bus;

@Repository
public interface BusRepository extends JpaRepository<Bus, Integer> {

    List<Bus> findByBusType(String busType);

}
