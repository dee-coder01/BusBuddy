package com.travellers_apis.nomadic_bus.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travellers_apis.nomadic_bus.commons.AdminException;
import com.travellers_apis.nomadic_bus.commons.RouteException;
import com.travellers_apis.nomadic_bus.models.Bus;
import com.travellers_apis.nomadic_bus.models.Route;
import com.travellers_apis.nomadic_bus.repositories.RouteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouteService {
    private static final String INVALID_USER_KEY = "User key is not correct! Please provide a valid key.";
    private static final Logger logger = LoggerFactory.getLogger(RouteService.class);
    final RouteRepository routeRepository;
    final UserSessionService sessionService;

    public Route shortestRoute(String source, String destination) {
        Map<String, Boolean> visited = new HashMap<>();
        if (hasPath(getRouteGraph(), source, destination, visited))
            return null;
        else
            return new Route();
    }

    public boolean isRouteAvailable(String source, String destination) {
        Map<String, Boolean> visited = new HashMap<>();
        return hasPath(getRouteGraph(), source, destination, visited);
    }

    private boolean hasPath(Map<String, List<Route>> graph, String source, String destination,
            Map<String, Boolean> visited) {
        if (source.equals(destination))
            return true;
        if (graph.get(source) == null)
            return false;
        visited.put(source, true);
        for (Route route : graph.get(source)) {
            if (visited.get(route.getRouteTo()) != null && visited.get(route.getRouteTo()))
                continue;
            boolean found = hasPath(graph, route.getRouteTo(), destination, visited);
            if (found)
                return found;
        }
        return false;
    }

    private Map<String, List<Route>> getRouteGraph() {
        List<Route> list = routeRepository.findAll();
        Map<String, List<Route>> graph = new HashMap<>();
        for (Route route : list) {
            List<Route> routeList = graph.get(route.getRouteFrom());
            if (routeList == null) {
                routeList = new ArrayList<>();
            }
            Route r = new Route(route.getRouteFrom(), route.getRouteTo(), route.getDistance());
            routeList.add(r);
            graph.put(route.getRouteFrom(), routeList);

            routeList = graph.get(route.getRouteTo());
            if (routeList == null) {
                routeList = new ArrayList<>();
            }
            r = new Route(route.getRouteTo(), route.getRouteFrom(), route.getDistance());
            routeList.add(r);
            graph.put(route.getRouteTo(), routeList);
        }
        return graph;
    }

    public List<List<Route>> getAllRouteFromSourceToDestination(String source, String destination) {
        Map<String, List<Route>> routeGraph = getRouteGraph();
        List<List<Route>> routes = new ArrayList<>();
        Map<String, Boolean> visited = new HashMap<>();
        routeFinder(routeGraph, source, destination, routes, visited, new ArrayList<>());
        return routes.isEmpty() ? null : routes;
    }

    private void routeFinder(Map<String, List<Route>> routeGraph, String source, String destination,
            List<List<Route>> routes, Map<String, Boolean> visited, List<Route> routeSoFar) {
        if (source.equals(destination)) {
            routes.add(List.copyOf(routeSoFar));
            routeSoFar = new ArrayList<>();
        }
        visited.put(source, true);
        for (Route route : routeGraph.get(source)) {
            if (visited.get(route.getRouteTo()) != null && visited.get(route.getRouteTo()))
                continue;
            routeSoFar.add(route);
            routeFinder(routeGraph, route.getRouteTo(), destination, routes, visited, routeSoFar);
            routeSoFar.remove(route);
        }
        visited.put(source, false);
    }

    public void bfs(String source, String destination) {
        Map<String, List<Route>> routeGraph = getRouteGraph();
        Set<String> visited = new HashSet<>();
        Queue<String> q = new LinkedList<>();
        q.add(source);
        while (!q.isEmpty()) {
            String src = q.remove();
            if (src.equals(destination))
                break;
            visited.add(src);
            for (Route route : routeGraph.get(src)) {
                if (!visited.contains(route.getRouteTo())) {
                    q.add(route.getRouteTo());
                    logger.info(route.getRouteTo());
                }
            }
        }
    }

    @Transactional(readOnly = true)
    public Route getRouteFromSourceToDestination(String source, String destination) {
        return routeRepository.findByRouteFromAndRouteTo(source, destination)
                .orElseThrow(() -> new RouteException("Route not found."));
    }

    @Transactional(readOnly = true)
    public boolean routeExistsBetweenSourceAndDestination(String source, String destination) {
        return routeRepository.findByRouteFromAndRouteTo(source, destination).isPresent();
    }

    @Transactional
    public Route addRoute(Route route) {
        // boolean isValidKey = sessionService.validateUserKey(key);
        // if (!isValidKey)
        // throw new AdminException(INVALID_USER_KEY);
        try {
            return getRouteFromSourceToDestination(route.getRouteFrom(), route.getRouteTo());
        } catch (RouteException e) {
            route.setBusList(new ArrayList<>());
            return routeRepository.save(route);
        }
    }

    @Transactional(readOnly = true)
    public List<Route> viewAllRoute() {
        List<Route> routes = routeRepository.findAll();
        if (routes.isEmpty())
            throw new RouteException("No route available");
        else
            return routes;
    }

    @Transactional(readOnly = true)
    public Route viewRoute(int routeId) {
        return routeRepository.findById(routeId)
                .orElseThrow(() -> new RouteException("There is no route present of this  routeId :" + routeId));
    }

    @Transactional
    public Route updateRoute(Route route, String key) {
        boolean isValidKey = sessionService.validateUserKey(key);
        if (isValidKey)
            throw new AdminException(INVALID_USER_KEY);
        Route presentRoute = routeRepository.findById(route.getRouteID())
                .orElseThrow(() -> new RouteException("Route doesn't exist of  this routeId : " + route.getRouteID()));
        List<Bus> busList = presentRoute.getBusList();
        if (!busList.isEmpty())
            throw new RouteException("Cannot update running route! Buses are already scheduled in the route.");
        return routeRepository.save(route);

    }

    @Transactional
    public void deleteRoute(int routeID, String key) {
        boolean isValidKey = sessionService.validateUserKey(key);
        if (isValidKey)
            throw new AdminException(INVALID_USER_KEY);
        Route existingRoute = routeRepository.findById(routeID)
                .orElseThrow(() -> new RouteException("Invalid route id."));
        routeRepository.deleteById(existingRoute.getRouteID());
    }
}
