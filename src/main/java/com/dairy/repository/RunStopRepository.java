package com.dairy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dairy.entity.RunStop;

public interface RunStopRepository extends JpaRepository<RunStop, Long> {

    List<RunStop> findByRunIdOrderByRouteStop_SequenceNoAsc(Long runId);
}
