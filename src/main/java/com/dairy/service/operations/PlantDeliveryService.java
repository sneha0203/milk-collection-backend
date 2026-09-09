package com.dairy.service.operations;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dairy.entity.ChillingPlant;
import com.dairy.entity.PlantDelivery;
import com.dairy.entity.Run;
import com.dairy.entity.RunStatus;
import com.dairy.entity.SpoilageResult;
import com.dairy.repository.ChillingPlantRepository;
import com.dairy.repository.PlantDeliveryRepository;
import com.dairy.repository.RunRepository;

@Service
public class PlantDeliveryService {

    private final PlantDeliveryRepository plantDeliveryRepository;
    private final RunRepository runRepository;
    private final ChillingPlantRepository chillingPlantRepository;
    private final SpoilageService spoilageService;

    public PlantDeliveryService(PlantDeliveryRepository plantDeliveryRepository,
                                 RunRepository runRepository,
                                 ChillingPlantRepository chillingPlantRepository,
                                 SpoilageService spoilageService) {
        this.plantDeliveryRepository = plantDeliveryRepository;
        this.runRepository = runRepository;
        this.chillingPlantRepository = chillingPlantRepository;
        this.spoilageService = spoilageService;
    }

    /**
     * Records that a tanker reached the plant, triggers spoilage evaluation
     * for every stop in that run, and marks the run as completed.
     */
    @Transactional
    public PlantDelivery recordDelivery(Long runId, Long chillingPlantId) {

        Run run = runRepository.findById(runId)
                .orElseThrow(() -> new IllegalArgumentException("Run not found: " + runId));

        ChillingPlant plant = chillingPlantRepository.findById(chillingPlantId)
                .orElseThrow(() -> new IllegalArgumentException("ChillingPlant not found: " + chillingPlantId));

        plantDeliveryRepository.findByRunId(runId).ifPresent(existing -> {
            throw new IllegalStateException("Plant delivery already recorded for run " + runId);
        });

        LocalDateTime deliveredAt = LocalDateTime.now();

        PlantDelivery delivery = new PlantDelivery(run, plant, deliveredAt);
        plantDeliveryRepository.save(delivery);

        // Evaluate spoilage for every stop now that we know delivery time
        List<SpoilageResult> results = spoilageService.evaluateSpoilage(runId, deliveredAt);

        // Mark run completed
        run.setStatus(RunStatus.COMPLETED);
        run.setCompletedAt(deliveredAt);
        runRepository.save(run);

        System.out.println("Plant delivery recorded for run " + runId + ". "
                + results.size() + " stops evaluated, "
                + results.stream().filter(SpoilageResult::getRejected).count() + " rejected.");

        return delivery;
    }
}