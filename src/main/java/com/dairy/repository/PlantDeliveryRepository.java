package com.dairy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dairy.entity.PlantDelivery;

public interface PlantDeliveryRepository extends JpaRepository<PlantDelivery, Long> {

    Optional<PlantDelivery> findByRunId(Long runId);
}
