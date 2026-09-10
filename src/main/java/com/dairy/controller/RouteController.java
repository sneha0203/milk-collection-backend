package com.dairy.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dairy.entity.Route;
import com.dairy.entity.RunType;
import com.dairy.service.planning.RouteGeneratorService;

@RestController
public class RouteController {

    private final RouteGeneratorService routeGeneratorService;

    public RouteController(RouteGeneratorService routeGeneratorService) {
        this.routeGeneratorService = routeGeneratorService;
    }
 
    /** Generates route templates for every tanker for the given run type (MORNING/EVENING) 
    and returns the list of created Route objects**/
    @PostMapping("/routes/generate")
    public List<Route> generateRoutes(@RequestParam RunType runType) {
        return routeGeneratorService.generateRoutes(runType);
    }
}
