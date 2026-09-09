package com.dairy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dairy.entity.Farmer;

public interface FarmerRepository extends JpaRepository<Farmer, Long> {

    // This is the key method - gives you "all farmers at this collection point"
    // for free, just by naming the method this way. Spring generates the SQL.
    List<Farmer> findByCollectionPointId(Long collectionPointId);

    List<Farmer> findByVillageId(Long villageId);
}