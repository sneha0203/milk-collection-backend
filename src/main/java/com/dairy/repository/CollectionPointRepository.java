package com.dairy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dairy.entity.CollectionPoint;

public interface CollectionPointRepository extends JpaRepository<CollectionPoint, Long> {

    List<CollectionPoint> findByVillageId(Long villageId);
}
