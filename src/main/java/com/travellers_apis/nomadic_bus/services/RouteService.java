package com.travellers_apis.nomadic_bus.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travellers_apis.nomadic_bus.commons.AdminException;
import com.travellers_apis.nomadic_bus.commons.RouteException;
import com.travellers_apis.nomadic_bus.models.Bus;
import com.travellers_apis.nomadic_bus.models.Route;
import com.travellers_apis.nomadic_bus.models.UserSession;
import com.travellers_apis.nomadic_bus.repositories.RouteRepository;
import com.travellers_apis.nomadic_bus.repositories.UserLoginRepo;

import jakarta.validation.Valid;

@Service
public class RouteService {
    @Autowired
    private RouteRepository routeRepository;

    private UserLoginRepo userLoginRepo;

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
        return routes;
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
        while (q.size() > 0) {
            String src = q.remove();
            if (visited.contains(src))
                continue;
            visited.add(src);
            System.out.println(visited);
            for (Route route : routeGraph.get(src)) {
                if (!visited.contains(route.getRouteTo())) {
                    q.add(route.getRouteTo());
                    System.out.println(route.getRouteTo());
                }
            }
        }
    }

    public Route getRouteFromSourceToDestination(String source, String destination) {
        return routeRepository.findByRouteFromAndRouteTo(source, destination);
    }

    public Route addRoute(@Valid Route route, String key) throws AdminException, RouteException {
        UserSession session = userLoginRepo.findByUuid(key);
        if (session == null)
            throw new AdminException("User key is not correct! Please provide a valid key.");
        Route newRoute = getRouteFromSourceToDestination(route.getRouteFrom(), route.getRouteTo());
        if (newRoute == null) {
            route.setBusList(new ArrayList<>());
            return routeRepository.save(route);
        } else {
            throw new RouteException("Route already exists.");
        }
    }

    public List<Route> viewAllRoute() throws RouteException {
        List<Route> routes = routeRepository.findAll();
        if (routes.isEmpty())
            throw new RouteException("No route available");
        else
            return routes;
    }

    public Route viewRoute(int routeId) throws RouteException {
        Optional<Route> opt = routeRepository.findById(routeId);
        return opt.orElseThrow(() -> new RouteException("There is no route present of this  routeId :" + routeId));
    }

    public Route updateRoute(Route route, String key) throws RouteException, AdminException {
        UserSession loggedInAdmin = userLoginRepo.findByUuid(key);
        if (loggedInAdmin == null) {
            throw new AdminException("Please provide a valid id to add route !");
        }
        Optional<Route> existedRoute = routeRepository.findById(route.getRouteID());
        if (existedRoute.isPresent()) {
            Route presentRoute = existedRoute.get();
            List<Bus> busList = presentRoute.getBusList();
            if (!busList.isEmpty())
                throw new RouteException("Cannot update running route! Buses are already scheduled in the route.");
            return routeRepository.save(route);
        } else
            throw new RouteException("Route doesn't exist of  this routeId : " + route.getRouteID());
    }

    public Route deleteRoute(int routeID, String key) throws RouteException, AdminException {
        UserSession loggedInAdmin = userLoginRepo.findByUuid(key);
        if (loggedInAdmin == null) {
            throw new AdminException("Please provide a valid id to add route !");
        }
        Optional<Route> route = routeRepository.findById(routeID);
        if (route.isPresent()) {
            Route existingRoute = route.get();
            routeRepository.delete(existingRoute);
            return existingRoute;
        } else
            throw new RouteException("There is no route of this routeId : " + routeID);
    }
}
