package com.dairy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dairy.entity.Farmer;

public interface FarmerRepository extends JpaRepository<Farmer, Long> {


    List<Farmer> findByCollectionPointId(Long collectionPointId);

    List<Farmer> findByVillageId(Long villageId);
}