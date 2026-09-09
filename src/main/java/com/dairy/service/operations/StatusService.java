package com.dairy.service.operations;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dairy.dto.TankerStatusResponse;
import com.dairy.entity.Run;
import com.dairy.entity.RunStop;
import com.dairy.entity.StopStatus;
import com.dairy.repository.RunRepository;
import com.dairy.repository.RunStopRepository;

@Service
public class StatusService {

    private final RunRepository runRepository;
    private final RunStopRepository runStopRepository;

    public StatusService(RunRepository runRepository, RunStopRepository runStopRepository) {
        this.runRepository = runRepository;
        this.runStopRepository = runStopRepository;
    }

    /**
     * Core "where is the tanker" logic. Looks at check-in events only -
     * no GPS. Finds the last ARRIVED/DEPARTED stop and the next PENDING one.
     */
    public TankerStatusResponse getStatus(Long runId) {

        Run run = runRepository.findById(runId)
                .orElseThrow(() -> new IllegalArgumentException("Run not found: " + runId));

        List<RunStop> stops = runStopRepository.findByRunIdOrderByRouteStop_SequenceNoAsc(runId);

        TankerStatusResponse response = new TankerStatusResponse();
        response.setRunId(run.getId());
        response.setTankerRegistrationNumber(run.getRoute().getTanker().getRegistrationNumber());
        response.setRunStatus(run.getStatus().name());

        RunStop lastConfirmed = null;
        RunStop nextPending = null;

        for (RunStop stop : stops) {
            if (stop.getStatus() == StopStatus.ARRIVED || stop.getStatus() == StopStatus.DEPARTED) {
                lastConfirmed = stop; // keeps overwriting - ends up as the LAST one in sequence order
            }
            if (stop.getStatus() == StopStatus.PENDING && nextPending == null) {
                nextPending = stop; // first PENDING stop found, in sequence order
            }
        }

        if (lastConfirmed != null) {
            response.setLastConfirmedStopId(lastConfirmed.getId());
            response.setLastConfirmedCollectionPointId(lastConfirmed.getRouteStop().getCollectionPoint().getId());
            response.setLastConfirmedStatus(lastConfirmed.getStatus().name());
            response.setLastConfirmedAt(
                    lastConfirmed.getStatus() == StopStatus.DEPARTED
                            ? lastConfirmed.getActualDepartureTime()
                            : lastConfirmed.getActualArrivalTime()
            );
        }

        if (nextPending != null) {
            response.setNextStopId(nextPending.getId());
            response.setNextCollectionPointId(nextPending.getRouteStop().getCollectionPoint().getId());
            response.setNextStopSequenceNo(nextPending.getRouteStop().getSequenceNo());

            // Simple ETA: use the difference between planned offsets of the
            // last confirmed stop and the next stop. Rough but reasonable
            // without real-time GPS.
            int plannedOffsetForNext = nextPending.getRouteStop().getPlannedArrivalOffsetMinutes();
            int plannedOffsetForLast = lastConfirmed != null
                    ? lastConfirmed.getRouteStop().getPlannedArrivalOffsetMinutes()
                    : 0;
            response.setEstimatedMinutesToNextStop(plannedOffsetForNext - plannedOffsetForLast);
        }

        return response;
    }

    /**
     * Farmer-facing: has MY milk been picked up yet, for a given run?
     * Finds the RunStop whose collection point matches the farmer's point.
     */
    public String getPickupStatusForCollectionPoint(Long runId, Long collectionPointId) {
        List<RunStop> stops = runStopRepository.findByRunIdOrderByRouteStop_SequenceNoAsc(runId);

        return stops.stream()
                .filter(s -> s.getRouteStop().getCollectionPoint().getId().equals(collectionPointId))
                .findFirst()
                .map(s -> s.getStatus().name())
                .orElse("NOT_ON_THIS_ROUTE");
    }
}