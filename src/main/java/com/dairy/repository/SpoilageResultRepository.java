package com.dairy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dairy.entity.SpoilageResult;

public interface SpoilageResultRepository extends JpaRepository<SpoilageResult, Long> {

    Optional<SpoilageResult> findByRunStopId(Long runStopId);

    List<SpoilageResult> findByRejectedTrue();
}