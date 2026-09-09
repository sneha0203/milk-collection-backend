package com.dairy.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "run_stops")
public class RunStop {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "run_id", nullable = false)
    private Run run;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_stop_id", nullable = false)
    private RouteStop routeStop;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StopStatus status;
    
    private LocalDateTime actualArrivalTime;

    private LocalDateTime actualDepartureTime;

    public RunStop() {
    }

    public RunStop(Run run, RouteStop routeStop) {
        this.run = run;
        this.routeStop = routeStop;
        this.status = StopStatus.PENDING;
    }

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public Run getRun() {
        return run;
    }

    public void setRun(Run run) {
        this.run = run;
    }

    public RouteStop getRouteStop() {
        return routeStop;
    }

    public void setRouteStop(RouteStop routeStop) {
        this.routeStop = routeStop;
    }
    
    public StopStatus getStatus() {
        return status;
    }

    public void setStatus(StopStatus status) {
        this.status = status;
    }

    public LocalDateTime getActualArrivalTime() {
        return actualArrivalTime;
    }

    public void setActualArrivalTime(LocalDateTime actualArrivalTime) {
        this.actualArrivalTime = actualArrivalTime;
    }
    
    public LocalDateTime getActualDepartureTime() {
        return actualDepartureTime;
    }

    public void setActualDepartureTime(LocalDateTime actualDepartureTime) {
        this.actualDepartureTime = actualDepartureTime;
    }

}
