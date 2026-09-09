package com.dairy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dairy.entity.RunStop;

public interface RunStopRepository extends JpaRepository<RunStop, Long> {

    // This is central to your status endpoint - all stops for a run, in order
    List<RunStop> findByRunIdOrderByRouteStop_SequenceNoAsc(Long runId);
}
