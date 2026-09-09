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
@Table(name="farmers")
public class Farmer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "village_id", nullable = false)
	private Village village;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_point_id", nullable = false)
	private CollectionPoint collectionPoint;
	
	@Column(nullable = false)
	private Double avgMilkQtyLiters;
	
	private String phoneNumber;
	
	public Farmer() {
		
	}
	
	public Farmer(String name, Village village, CollectionPoint collectionPoint, Double avgMilkQtyLiters
			) {
		super();
		this.name = name;
		this.village = village;
		this.collectionPoint = collectionPoint;
		this.avgMilkQtyLiters = avgMilkQtyLiters;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Village getVillage() {
		return village;
	}
	public void setVillage(Village village) {
		this.village = village;
	}
	public CollectionPoint getCollectionPoint() {
		return collectionPoint;
	}
	public void setCollectionPoint(CollectionPoint collectionPoint) {
		this.collectionPoint = collectionPoint;
	}
	public Double getAvgMilkQtyLiters() {
		return avgMilkQtyLiters;
	}
	public void setAvgMilkQtyLiters(Double avgMilkQtyLiters) {
		this.avgMilkQtyLiters = avgMilkQtyLiters;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	

}
