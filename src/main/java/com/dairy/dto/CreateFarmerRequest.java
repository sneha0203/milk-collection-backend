package com.dairy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateFarmerRequest {

    @NotBlank
    private String name;

    @NotNull
    private Long villageId;

    @NotNull
    private Long collectionPointId;

    @NotNull
    @Positive
    private Double avgMilkQtyLiters;

    private String phoneNumber;

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getVillageId() {
        return villageId;
    }

    public void setVillageId(Long villageId) {
        this.villageId = villageId;
    }

    public Long getCollectionPointId() {
        return collectionPointId;
    }

    public void setCollectionPointId(Long collectionPointId) {
        this.collectionPointId = collectionPointId;
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