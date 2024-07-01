package com.travellers_apis.nomadic_bus.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travellers_apis.nomadic_bus.models.Route;

@Repository
public interface RouteRepository extends JpaRepository<Route, Integer> {
    Optional<Route> findByRouteFromAndRouteTo(String routeFrom, String routeTo);
}
