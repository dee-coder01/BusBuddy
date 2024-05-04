package com.travellers_apis.nomadic_bus.repositories;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.travellers_apis.nomadic_bus.models.Reservation;

import jakarta.transaction.Transactional;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Integer> {
    String cancellationQuery = "delete from reservation where booked_seat = :booked_seat and bus_bus_id = :bus_bus_id and journey_date = :journey_date and source = :source and destination = :destination";

    @Modifying
    @Transactional
    @Query(value = cancellationQuery, nativeQuery = true)
    public void deleteByReservationInfo(@Param("booked_seat") Integer bookedSeat,
            @Param("bus_bus_id") Integer busId, @Param("journey_date") LocalDate journeyDate,
            @Param("source") String source, @Param("destination") String destination);
}
