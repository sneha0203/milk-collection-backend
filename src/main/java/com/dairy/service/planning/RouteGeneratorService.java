package com.dairy.service.planning;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dairy.entity.CollectionPoint;
import com.dairy.entity.Route;
import com.dairy.entity.RouteStop;
import com.dairy.entity.Tanker;
import com.dairy.entity.RunType;
import com.dairy.repository.CollectionPointRepository;
import com.dairy.repository.RouteRepository;
import com.dairy.repository.RouteStopRepository;
import com.dairy.repository.TankerRepository;

@Service
public class RouteGeneratorService {

    private final CollectionPointRepository collectionPointRepository;
    private final TankerRepository tankerRepository;
    private final RouteRepository routeRepository;
    private final RouteStopRepository routeStopRepository;

    // Assumptions - documented here AND in the README
    private static final double AVG_SPEED_KMPH = 30.0;
    private static final double KM_PER_DEGREE = 111.0; // rough lat/lng-to-km conversion

    @Value("${dairy.spoilage-threshold-minutes}")
    private int spoilageThresholdMinutes;

    public RouteGeneratorService(CollectionPointRepository collectionPointRepository,
                                  TankerRepository tankerRepository,
                                  RouteRepository routeRepository,
                                  RouteStopRepository routeStopRepository) {
        this.collectionPointRepository = collectionPointRepository;
        this.tankerRepository = tankerRepository;
        this.routeRepository = routeRepository;
        this.routeStopRepository = routeStopRepository;
    }

    /**
     * Generates one Route per tanker for the given run type (MORNING/EVENING).
     */
    @Transactional
    public List<Route> generateRoutes(RunType runType) {

        List<CollectionPoint> allPoints = collectionPointRepository.findAll();
        List<Tanker> tankers = tankerRepository.findAll();

        if (allPoints.isEmpty() || tankers.isEmpty()) {
            throw new IllegalStateException("Cannot generate routes: no collection points or tankers found.");
        }

        List<List<CollectionPoint>> clusters = splitIntoClusters(allPoints, tankers.size());
        List<Route> createdRoutes = new ArrayList<>();

        for (int i = 0; i < tankers.size(); i++) {
            Tanker tanker = tankers.get(i);
            List<CollectionPoint> clusterPoints = clusters.get(i);

            if (clusterPoints.isEmpty()) {
                continue; // this tanker gets no stops this run - acceptable if points < tankers
            }

            List<CollectionPoint> orderedStops = orderByNearestNeighbor(clusterPoints);

            Route route = new Route(tanker, runType);
            routeRepository.save(route);

            saveRouteStops(route, orderedStops);
            createdRoutes.add(route);
        }

        return createdRoutes;
    }

    /**
     * Simple even split: point 0 -> tanker 0, point 1 -> tanker 1, ... point N -> tanker (N % numTankers).
     * Not geography-aware, but combined with nearest-neighbor ordering within
     * each cluster, it produces reasonable routes without real clustering math.
     */
    private List<List<CollectionPoint>> splitIntoClusters(List<CollectionPoint> points, int numClusters) {
        List<List<CollectionPoint>> clusters = new ArrayList<>();
        for (int i = 0; i < numClusters; i++) {
            clusters.add(new ArrayList<>());
        }
        for (int i = 0; i < points.size(); i++) {
            clusters.get(i % numClusters).add(points.get(i));
        }
        return clusters;
    }

    /**
     * Classic nearest-neighbor heuristic: start anywhere, repeatedly jump to
     * the closest unvisited point. Not optimal, but simple, fast, and easy
     * to explain live in an interview.
     */
    private List<CollectionPoint> orderByNearestNeighbor(List<CollectionPoint> points) {
        List<CollectionPoint> remaining = new ArrayList<>(points);
        List<CollectionPoint> ordered = new ArrayList<>();

        CollectionPoint current = remaining.remove(0);
        ordered.add(current);

        while (!remaining.isEmpty()) {
            CollectionPoint nearest = null;
            double minDistance = Double.MAX_VALUE;

            for (CollectionPoint candidate : remaining) {
                double distance = distanceKm(current, candidate);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearest = candidate;
                }
            }

            ordered.add(nearest);
            remaining.remove(nearest);
            current = nearest;
        }

        return ordered;
    }

    /**
     * Walks the ordered stops, accumulating travel time, and saves each as a
     * RouteStop. Also flags (via console warning) any stop whose cumulative
     * time exceeds the spoilage threshold
     */
    private void saveRouteStops(Route route, List<CollectionPoint> orderedStops) {
        double cumulativeMinutes = 0;
        CollectionPoint previous = null;
        int sequenceNo = 1;

        for (CollectionPoint point : orderedStops) {
            if (previous != null) {
                double distanceKm = distanceKm(previous, point);
                double travelMinutes = (distanceKm / AVG_SPEED_KMPH) * 60;
                cumulativeMinutes += travelMinutes;
            }

            int offsetMinutes = (int) Math.round(cumulativeMinutes);

            if (offsetMinutes > spoilageThresholdMinutes) {
                System.out.println("WARNING: Route " + route.getId() + " stop " + sequenceNo
                        + " (point " + point.getId() + ") planned at " + offsetMinutes
                        + " min, exceeds spoilage threshold of " + spoilageThresholdMinutes + " min.");
            }

            RouteStop stop = new RouteStop(route, point, sequenceNo, offsetMinutes);
            routeStopRepository.save(stop);

            previous = point;
            sequenceNo++;
        }
    }

    /**
     * Simplified flat-earth distance using lat/lng degree differences.
     */
    private double distanceKm(CollectionPoint a, CollectionPoint b) {
        double dLat = (a.getLatitude() - b.getLatitude()) * KM_PER_DEGREE;
        double dLng = (a.getLongitude() - b.getLongitude()) * KM_PER_DEGREE;
        return Math.sqrt(dLat * dLat + dLng * dLng);
    }
}