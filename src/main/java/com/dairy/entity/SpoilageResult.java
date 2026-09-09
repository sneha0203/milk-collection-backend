package com.dairy.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "spoilage_results")
public class SpoilageResult {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "run_stop_id", nullable = false, unique = true)
    private RunStop runStop;

    @Column(nullable = false)
    private Integer elapsedMinutes;

    @Column(nullable = false)
    private Boolean rejected;

    public SpoilageResult() {
    }
    
    public SpoilageResult(RunStop runStop, Integer elapsedMinutes, Boolean rejected) {
        this.runStop = runStop;
        this.elapsedMinutes = elapsedMinutes;
        this.rejected = rejected;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RunStop getRunStop() {
        return runStop;
    }
    
    public void setRunStop(RunStop runStop) {
        this.runStop = runStop;
    }

    public Integer getElapsedMinutes() {
        return elapsedMinutes;
    }

    public void setElapsedMinutes(Integer elapsedMinutes) {
        this.elapsedMinutes = elapsedMinutes;
    }
    
    public Boolean getRejected() {
        return rejected;
    }

    public void setRejected(Boolean rejected) {
        this.rejected = rejected;
    }

    

}
