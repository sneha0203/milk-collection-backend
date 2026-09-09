package com.dairy.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dairy.dto.CreateCollectionPointRequest;
import com.dairy.entity.CollectionPoint;
import com.dairy.entity.Village;
import com.dairy.repository.CollectionPointRepository;
import com.dairy.repository.VillageRepository;

@RestController
public class CollectionPointController {

    private final CollectionPointRepository collectionPointRepository;
    private final VillageRepository villageRepository;

    public CollectionPointController(CollectionPointRepository collectionPointRepository,
                                      VillageRepository villageRepository) {
        this.collectionPointRepository = collectionPointRepository;
        this.villageRepository = villageRepository;
    }

    // POST /collection-points
    @PostMapping("/collection-points")
    public CollectionPoint createCollectionPoint(@Valid @RequestBody CreateCollectionPointRequest request) {
        Village village = villageRepository.findById(request.getVillageId())
                .orElseThrow(() -> new IllegalArgumentException("Village not found: " + request.getVillageId()));

        CollectionPoint point = new CollectionPoint(village, request.getLatitude(), request.getLongitude());
        return collectionPointRepository.save(point);
    }
}