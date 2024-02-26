package com.travellers_apis.nomadic_bus.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travellers_apis.nomadic_bus.models.Route;
import com.travellers_apis.nomadic_bus.repositories.RouteRepository;

@Service
public class RouteService {
    @Autowired
    private RouteRepository routeRepository;

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
}
