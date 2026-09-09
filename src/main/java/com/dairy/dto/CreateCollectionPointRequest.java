package com.dairy.dto;

import jakarta.validation.constraints.NotNull;

public class CreateCollectionPointRequest {

    @NotNull
    private Long villageId;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    public Long getVillageId() {
        return villageId;
    }

    public void setVillageId(Long villageId) {
        this.villageId = villageId;
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
}