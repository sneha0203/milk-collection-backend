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
@Table(name = "routes")
public class Route {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tanker_id", nullable = false)
    private Tanker tanker;

    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RunType runType;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Route() {
    }

    public Route(Tanker tanker, RunType runType) {
        this.tanker = tanker;
        this.runType = runType;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
    	return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tanker getTanker() {
        return tanker;
    }

    public void setTanker(Tanker tanker) {
        this.tanker = tanker;
    }
    public RunType getRunType() {
        return runType;
    }

    public void setRunType(RunType runType) {
        this.runType = runType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    }


