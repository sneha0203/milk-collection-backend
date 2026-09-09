package com.dairy.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dairy.entity.Run;

public interface RunRepository extends JpaRepository<Run, Long> {

    List<Run> findByRouteId(Long routeId);

    Optional<Run> findByRouteIdAndRunDate(Long routeId, LocalDate runDate);
}
