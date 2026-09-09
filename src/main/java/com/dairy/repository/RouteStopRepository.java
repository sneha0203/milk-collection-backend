package com.dairy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dairy.entity.RouteStop;

public interface RouteStopRepository extends JpaRepository<RouteStop, Long> {

    // Ordered stops for a route - this is how you rebuild the full sequence
    List<RouteStop> findByRouteIdOrderBySequenceNoAsc(Long routeId);
}
