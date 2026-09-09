package com.dairy.dto;

import java.time.LocalDateTime;

public class TankerStatusResponse {

    private Long runId;
    private String tankerRegistrationNumber;
    private String runStatus;

    private Long lastConfirmedStopId;
    private Long lastConfirmedCollectionPointId;
    private String lastConfirmedStatus;
    private LocalDateTime lastConfirmedAt;

    private Long nextStopId;
    private Long nextCollectionPointId;
    private Integer nextStopSequenceNo;
    private Integer estimatedMinutesToNextStop;

    // No-arg constructor + all getters/setters below

    public TankerStatusResponse() {
    }

    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
    }

    public String getTankerRegistrationNumber() {
        return tankerRegistrationNumber;
    }

    public void setTankerRegistrationNumber(String tankerRegistrationNumber) {
        this.tankerRegistrationNumber = tankerRegistrationNumber;
    }

    public String getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(String runStatus) {
        this.runStatus = runStatus;
    }

    public Long getLastConfirmedStopId() {
        return lastConfirmedStopId;
    }

    public void setLastConfirmedStopId(Long lastConfirmedStopId) {
        this.lastConfirmedStopId = lastConfirmedStopId;
    }

    public Long getLastConfirmedCollectionPointId() {
        return lastConfirmedCollectionPointId;
    }

    public void setLastConfirmedCollectionPointId(Long lastConfirmedCollectionPointId) {
        this.lastConfirmedCollectionPointId = lastConfirmedCollectionPointId;
    }

    public String getLastConfirmedStatus() {
        return lastConfirmedStatus;
    }

    public void setLastConfirmedStatus(String lastConfirmedStatus) {
        this.lastConfirmedStatus = lastConfirmedStatus;
    }

    public LocalDateTime getLastConfirmedAt() {
        return lastConfirmedAt;
    }

    public void setLastConfirmedAt(LocalDateTime lastConfirmedAt) {
        this.lastConfirmedAt = lastConfirmedAt;
    }

    public Long getNextStopId() {
        return nextStopId;
    }

    public void setNextStopId(Long nextStopId) {
        this.nextStopId = nextStopId;
    }

    public Long getNextCollectionPointId() {
        return nextCollectionPointId;
    }

    public void setNextCollectionPointId(Long nextCollectionPointId) {
        this.nextCollectionPointId = nextCollectionPointId;
    }

    public Integer getNextStopSequenceNo() {
        return nextStopSequenceNo;
    }

    public void setNextStopSequenceNo(Integer nextStopSequenceNo) {
        this.nextStopSequenceNo = nextStopSequenceNo;
    }

    public Integer getEstimatedMinutesToNextStop() {
        return estimatedMinutesToNextStop;
    }

    public void setEstimatedMinutesToNextStop(Integer estimatedMinutesToNextStop) {
        this.estimatedMinutesToNextStop = estimatedMinutesToNextStop;
    }
}