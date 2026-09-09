package com.dairy.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dairy.entity.PlantDelivery;
import com.dairy.service.operations.PlantDeliveryService;

import com.dairy.entity.Run;
import com.dairy.entity.RunStop;
import com.dairy.service.operations.RunService;

@RestController
public class RunController {

    private final RunService runService;
    
    private final PlantDeliveryService plantDeliveryService;

    public RunController(RunService runService, PlantDeliveryService plantDeliveryService) {
        this.runService = runService;
        this.plantDeliveryService = plantDeliveryService;
    }

    @PostMapping("/runs/start")
    public Run startRun(@RequestParam Long routeId,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate runDate) {
        return runService.startRun(routeId, runDate);
    }
 
    @PostMapping("/runs/stops/{runStopId}/arrive")
    public RunStop markArrived(@PathVariable Long runStopId) {
        return runService.markArrived(runStopId);
    }

    
    @PostMapping("/runs/stops/{runStopId}/depart")
    public RunStop markDeparted(@PathVariable Long runStopId) {
        return runService.markDeparted(runStopId);
    }

    
    @PostMapping("/runs/stops/{runStopId}/skip")
    public RunStop markSkipped(@PathVariable Long runStopId) {
        return runService.markSkipped(runStopId);
    }


    @PostMapping("/runs/{runId}/complete")
    public Run completeRun(@PathVariable Long runId) {
        return runService.completeRun(runId);
    }
    
    @PostMapping("/runs/{runId}/deliver")
    public PlantDelivery recordDelivery(@PathVariable Long runId, @RequestParam Long chillingPlantId) {
        return plantDeliveryService.recordDelivery(runId, chillingPlantId);
    }
}