package com.dairy.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="tankers")
public class Tanker {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
    private String registrationNumber;
    
	@Column(nullable = false)
    private Double capacityLiters;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRegistrationNumber() {
		return registrationNumber;
	}
	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	public Double getCapacityLiters() {
		return capacityLiters;
	}
	public void setCapacityLiters(Double capacityLiters) {
		this.capacityLiters = capacityLiters;
	}

	public Tanker() {
		
	}
	public Tanker(String registrationNumber, Double capacityLiters) {
		super();
		this.registrationNumber = registrationNumber;
		this.capacityLiters = capacityLiters;
	}
	
	
    
}
