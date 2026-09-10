package com.dairy.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dairy.dto.CreateFarmerRequest;
import com.dairy.entity.CollectionPoint;
import com.dairy.entity.Farmer;
import com.dairy.entity.Village;
import com.dairy.repository.CollectionPointRepository;
import com.dairy.repository.FarmerRepository;
import com.dairy.repository.VillageRepository;

@RestController
public class FarmerController {

    private final FarmerRepository farmerRepository;
    private final VillageRepository villageRepository;
    private final CollectionPointRepository collectionPointRepository;

    public FarmerController(FarmerRepository farmerRepository,
                             VillageRepository villageRepository,
                             CollectionPointRepository collectionPointRepository) {
        this.farmerRepository = farmerRepository;
        this.villageRepository = villageRepository;
        this.collectionPointRepository = collectionPointRepository;
    }

    // Creates a new farmer linked to an existing village and collection point.
    @PostMapping("/farmers")
    public Farmer createFarmer(@Valid @RequestBody CreateFarmerRequest request) {
        Village village = villageRepository.findById(request.getVillageId())
                .orElseThrow(() -> new IllegalArgumentException("Village not found: " + request.getVillageId()));

        CollectionPoint point = collectionPointRepository.findById(request.getCollectionPointId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "CollectionPoint not found: " + request.getCollectionPointId()));

        Farmer farmer = new Farmer(request.getName(), village, point, request.getAvgMilkQtyLiters());
        farmer.setPhoneNumber(request.getPhoneNumber());

        return farmerRepository.save(farmer);
    }
}