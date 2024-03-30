package com.travellers_apis.nomadic_bus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travellers_apis.nomadic_bus.models.Reservation;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Integer> {

}
