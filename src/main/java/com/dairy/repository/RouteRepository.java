package com.dairy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dairy.entity.Route;
import com.dairy.entity.RunType;

public interface RouteRepository extends JpaRepository<Route, Long> {

    List<Route> findByTankerId(Long tankerId);

    List<Route> findByRunType(RunType runType);
}
