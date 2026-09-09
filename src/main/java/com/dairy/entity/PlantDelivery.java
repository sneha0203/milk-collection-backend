package com.dairy.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "plant_deliveries")
public class PlantDelivery {
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;

	 @OneToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "run_id", nullable = false, unique = true)
	 private Run run;

	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "chilling_plant_id", nullable = false)
	 private ChillingPlant chillingPlant;

	 @Column(nullable = false)
	 private LocalDateTime deliveredAt;
	 
	 public PlantDelivery() {
	    }

	 public PlantDelivery(Run run, ChillingPlant chillingPlant, LocalDateTime deliveredAt) {
	        this.run = run;
	        this.chillingPlant = chillingPlant;
	        this.deliveredAt = deliveredAt;
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

	 public ChillingPlant getChillingPlant() {
	        return chillingPlant;
	 }

	 public void setChillingPlant(ChillingPlant chillingPlant) {
	        this.chillingPlant = chillingPlant;
	 }

	 public LocalDateTime getDeliveredAt() {
	        return deliveredAt;
	 }
	    
	 public void setDeliveredAt(LocalDateTime deliveredAt) {
	        this.deliveredAt = deliveredAt;
	 }
}
