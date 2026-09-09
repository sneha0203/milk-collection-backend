package com.dairy.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "route_stops")
public class RouteStop {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_point_id", nullable = false)
    private CollectionPoint collectionPoint;

    // Order of this stop within the route: 1, 2, 3...
    @Column(nullable = false)
    private Integer sequenceNo;
    
    @Column(nullable = false)
    private Integer plannedArrivalOffsetMinutes;

    public RouteStop() {
    }

    public RouteStop(Route route, CollectionPoint collectionPoint, Integer sequenceNo,
                      Integer plannedArrivalOffsetMinutes) {
        this.route = route;
        this.collectionPoint = collectionPoint;
        this.sequenceNo = sequenceNo;
        this.plannedArrivalOffsetMinutes = plannedArrivalOffsetMinutes;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
    
    public CollectionPoint getCollectionPoint() {
        return collectionPoint;
    }

    public void setCollectionPoint(CollectionPoint collectionPoint) {
        this.collectionPoint = collectionPoint;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }
    
    public Integer getPlannedArrivalOffsetMinutes() {
        return plannedArrivalOffsetMinutes;
    }

    public void setPlannedArrivalOffsetMinutes(Integer plannedArrivalOffsetMinutes) {
        this.plannedArrivalOffsetMinutes = plannedArrivalOffsetMinutes;
    }

}
