package com.dairy.service.operations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dairy.entity.Route;
import com.dairy.entity.RouteStop;
import com.dairy.entity.Run;
import com.dairy.entity.RunStatus;
import com.dairy.entity.RunStop;
import com.dairy.entity.StopStatus;
import com.dairy.repository.RouteRepository;
import com.dairy.repository.RouteStopRepository;
import com.dairy.repository.RunRepository;
import com.dairy.repository.RunStopRepository;

@Service
public class RunService {

    private final RouteRepository routeRepository;
    private final RouteStopRepository routeStopRepository;
    private final RunRepository runRepository;
    private final RunStopRepository runStopRepository;

    public RunService(RouteRepository routeRepository,
                       RouteStopRepository routeStopRepository,
                       RunRepository runRepository,
                       RunStopRepository runStopRepository) {
        this.routeRepository = routeRepository;
        this.routeStopRepository = routeStopRepository;
        this.runRepository = runRepository;
        this.runStopRepository = runStopRepository;
    }

    /**
     * Starts today's execution of a planned Route.
     * Creates one Run row, and one PENDING RunStop for every RouteStop in
     * the route - this "materializes" the plan into today's trackable trip.
     */
    @Transactional
    public Run startRun(Long routeId, LocalDate runDate) {

        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalArgumentException("Route not found: " + routeId));

        // Prevent starting the same route twice on the same day
        runRepository.findByRouteIdAndRunDate(routeId, runDate).ifPresent(existing -> {
            throw new IllegalStateException("Run already exists for route " + routeId + " on " + runDate);
        });

        Run run = new Run(route, runDate);
        run.setStatus(RunStatus.IN_PROGRESS);
        run.setStartedAt(LocalDateTime.now());
        runRepository.save(run);

        List<RouteStop> plannedStops = routeStopRepository.findByRouteIdOrderBySequenceNoAsc(routeId);
        for (RouteStop routeStop : plannedStops) {
            RunStop runStop = new RunStop(run, routeStop); // starts as PENDING
            runStopRepository.save(runStop);
        }

        return run;
    }

    /**
     * Driver check-in: marks a stop as ARRIVED and records the real timestamp.
     * This is the core "no GPS needed" mechanism - status comes from these
     * manual events, not coordinates.
     */
    @Transactional
    public RunStop markArrived(Long runStopId) {
        RunStop runStop = getRunStopOrThrow(runStopId);

        if (runStop.getStatus() != StopStatus.PENDING) {
            throw new IllegalStateException("Stop " + runStopId + " is not PENDING (current: "
                    + runStop.getStatus() + ") - cannot mark arrived.");
        }

        runStop.setStatus(StopStatus.ARRIVED);
        runStop.setActualArrivalTime(LocalDateTime.now());
        return runStopRepository.save(runStop);
    }

    /**
     * Marks a stop as departed - milk has been picked up, tanker is moving
     * to the next stop.
     */
    @Transactional
    public RunStop markDeparted(Long runStopId) {
        RunStop runStop = getRunStopOrThrow(runStopId);

        if (runStop.getStatus() != StopStatus.ARRIVED) {
            throw new IllegalStateException("Stop " + runStopId + " must be ARRIVED before it can be DEPARTED "
                    + "(current: " + runStop.getStatus() + ").");
        }

        runStop.setStatus(StopStatus.DEPARTED);
        runStop.setActualDepartureTime(LocalDateTime.now());
        return runStopRepository.save(runStop);
    }

    /**
     * Marks a stop as skipped (e.g., farmer had no milk that day, or point
     * was inaccessible). Valid from PENDING only.
     */
    @Transactional
    public RunStop markSkipped(Long runStopId) {
        RunStop runStop = getRunStopOrThrow(runStopId);

        if (runStop.getStatus() != StopStatus.PENDING) {
            throw new IllegalStateException("Stop " + runStopId + " must be PENDING to skip "
                    + "(current: " + runStop.getStatus() + ").");
        }

        runStop.setStatus(StopStatus.SKIPPED);
        return runStopRepository.save(runStop);
    }

    /**
     * Marks the whole run as completed - typically called after plant
     * delivery is recorded (Step 14).
     */
    @Transactional
    public Run completeRun(Long runId) {
        Run run = runRepository.findById(runId)
                .orElseThrow(() -> new IllegalArgumentException("Run not found: " + runId));

        run.setStatus(RunStatus.COMPLETED);
        run.setCompletedAt(LocalDateTime.now());
        return runRepository.save(run);
    }

    private RunStop getRunStopOrThrow(Long runStopId) {
        return runStopRepository.findById(runStopId)
                .orElseThrow(() -> new IllegalArgumentException("RunStop not found: " + runStopId));
    }
}