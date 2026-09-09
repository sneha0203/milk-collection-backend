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
@Table(name="collections_points")
public class CollectionPoint {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "village_id", nullable = false)
	private Village village;
	
	 @Column(nullable = false)
	 private Double latitude;

	 public Long getId() {
		return id;
	}

	 public void setId(Long id) {
		 this.id = id;
	 }

	 public Village getVillage() {
		 return village;
	 }

	 public void setVillage(Village village) {
		 this.village = village;
	 }

	 public Double getLatitude() {
		 return latitude;
	 }

	 public void setLatitude(Double latitude) {
		 this.latitude = latitude;
	 }

	 public Double getLongitude() {
		 return longitude;
	 }

	 public void setLongitude(Double longitude) {
		 this.longitude = longitude;
	 }

	 @Column(nullable = false)
	 private Double longitude;
	 
	 public CollectionPoint() {
		 
	 }

	 public CollectionPoint( Village village, Double latitude, Double longitude) {
		
		this.village = village;
		this.latitude = latitude;
		this.longitude = longitude;
	 }
	 
	 
}

