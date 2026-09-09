package com.dairy.service.operations;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dairy.entity.RunStop;
import com.dairy.entity.SpoilageResult;
import com.dairy.entity.StopStatus;
import com.dairy.repository.RunStopRepository;
import com.dairy.repository.SpoilageResultRepository;

@Service
public class SpoilageService {

    private final RunStopRepository runStopRepository;
    private final SpoilageResultRepository spoilageResultRepository;

    @Value("${dairy.spoilage-threshold-minutes}")
    private int spoilageThresholdMinutes;

    public SpoilageService(RunStopRepository runStopRepository,
                            SpoilageResultRepository spoilageResultRepository) {
        this.runStopRepository = runStopRepository;
        this.spoilageResultRepository = spoilageResultRepository;
    }

    /**
     * Called once plant delivery happens for a run. For every DEPARTED stop
     * in that run, compute elapsed time from actual pickup to plant delivery,
     * and decide if it's rejected.
     */
    @Transactional
    public List<SpoilageResult> evaluateSpoilage(Long runId, LocalDateTime deliveredAt) {

        List<RunStop> runStops = runStopRepository.findByRunIdOrderByRouteStop_SequenceNoAsc(runId);
        List<SpoilageResult> results = new java.util.ArrayList<>();

        for (RunStop runStop : runStops) {

            // Only evaluate stops that actually had milk picked up
            if (runStop.getStatus() != StopStatus.DEPARTED || runStop.getActualArrivalTime() == null) {
                continue;
            }

            long elapsedMinutes = Duration.between(runStop.getActualArrivalTime(), deliveredAt).toMinutes();
            boolean rejected = elapsedMinutes > spoilageThresholdMinutes;

            SpoilageResult result = new SpoilageResult(runStop, (int) elapsedMinutes, rejected);
            spoilageResultRepository.save(result);
            results.add(result);

            if (rejected) {
                System.out.println("REJECTED: RunStop " + runStop.getId() + " - elapsed "
                        + elapsedMinutes + " min (threshold " + spoilageThresholdMinutes + " min)");
            }
        }

        return results;
    }
}