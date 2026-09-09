package com.dairy.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.dairy.entity.Village;

public interface VillageRepository extends JpaRepository<Village, Long> {
}
