package com.dairy.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dairy.dto.TankerStatusResponse;
import com.dairy.service.operations.StatusService;

@RestController
public class StatusController {

    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    
    @GetMapping("/runs/{runId}/status")
    public TankerStatusResponse getStatus(@PathVariable Long runId) {
        return statusService.getStatus(runId);
    }

    
    @GetMapping("/runs/{runId}/pickup-status")
    public String getPickupStatus(@PathVariable Long runId, @RequestParam Long collectionPointId) {
        return statusService.getPickupStatusForCollectionPoint(runId, collectionPointId);
    }
}