package com.dairy.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.dairy.entity.ChillingPlant;
import com.dairy.entity.CollectionPoint;
import com.dairy.entity.Farmer;
import com.dairy.entity.Tanker;
import com.dairy.entity.Village;
import com.dairy.repository.ChillingPlantRepository;
import com.dairy.repository.CollectionPointRepository;
import com.dairy.repository.FarmerRepository;
import com.dairy.repository.TankerRepository;
import com.dairy.repository.VillageRepository;

/**
 * Runs ONCE automatically when the app starts (CommandLineRunner).
 * Generates fake but realistic-scale data so you don't have to insert
 * 1,400 farmers by hand.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final VillageRepository villageRepository;
    private final CollectionPointRepository collectionPointRepository;
    private final FarmerRepository farmerRepository;
    private final TankerRepository tankerRepository;
    private final ChillingPlantRepository chillingPlantRepository;

    private final Random random = new Random();

    // Constructor injection - Spring automatically provides these repositories
    public DataLoader(VillageRepository villageRepository,
                       CollectionPointRepository collectionPointRepository,
                       FarmerRepository farmerRepository,
                       TankerRepository tankerRepository,
                       ChillingPlantRepository chillingPlantRepository) {
        this.villageRepository = villageRepository;
        this.collectionPointRepository = collectionPointRepository;
        this.farmerRepository = farmerRepository;
        this.tankerRepository = tankerRepository;
        this.chillingPlantRepository = chillingPlantRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // Safety check: don't reseed if data already exists (e.g., app restarted)
        if (villageRepository.count() > 0) {
            System.out.println("Seed data already exists - skipping DataLoader.");
            return;
        }

        System.out.println("Seeding data...");

        // Base coordinates - roughly centered somewhere; villages/points are
        // scattered around this using small random offsets (simplified, not real geo)
        double baseLat = 12.9716;
        double baseLng = 77.5946;

        // 1. Chilling plant (just one for this demo)
        ChillingPlant plant = new ChillingPlant("Main Chilling Plant", baseLat, baseLng);
        chillingPlantRepository.save(plant);

        // 2. Tankers - 22 of them
        List<Tanker> tankers = new ArrayList<>();
        for (int i = 1; i <= 22; i++) {
            Tanker tanker = new Tanker("TN-" + String.format("%03d", i), 3000.0);
            tankers.add(tankerRepository.save(tanker));
        }

        // 3. Villages - 60 of them
        List<Village> villages = new ArrayList<>();
        for (int i = 1; i <= 60; i++) {
            Village village = new Village("Village-" + i);
            villages.add(villageRepository.save(village));
        }

        // 4. Collection points - a few per village, scattered around it
        List<CollectionPoint> allPoints = new ArrayList<>();
        for (Village village : villages) {
            int pointsInThisVillage = 2 + random.nextInt(2); // 2 or 3 points per village
            for (int p = 0; p < pointsInThisVillage; p++) {
                double lat = baseLat + (random.nextDouble() - 0.5) * 0.5;
                double lng = baseLng + (random.nextDouble() - 0.5) * 0.5;
                CollectionPoint point = new CollectionPoint(village, lat, lng);
                allPoints.add(collectionPointRepository.save(point));
            }
        }

        // 5. Farmers - ~1,400 total, distributed across collection points
        //    ~70% of points get 1 farmer, ~30% get 2 - this is what naturally
        //    creates the "shared collection point" scenario from the brief.
        int totalFarmers = 1400;
        int farmerCount = 0;
        int pointIndex = 0;

        while (farmerCount < totalFarmers) {
            CollectionPoint point = allPoints.get(pointIndex % allPoints.size());
            Village village = point.getVillage();

            int farmersAtThisPoint = random.nextDouble() < 0.7 ? 1 : 2;

            for (int f = 0; f < farmersAtThisPoint && farmerCount < totalFarmers; f++) {
                farmerCount++;
                String name = "Farmer-" + farmerCount;
                double avgMilk = 5 + random.nextDouble() * 15; // 5 to 20 liters
                Farmer farmer = new Farmer(name, village, point, avgMilk);
                farmerRepository.save(farmer);
            }

            pointIndex++;
        }

        System.out.println("Seeding complete: "
                + villages.size() + " villages, "
                + allPoints.size() + " collection points, "
                + tankers.size() + " tankers, "
                + farmerCount + " farmers.");
    }
}
