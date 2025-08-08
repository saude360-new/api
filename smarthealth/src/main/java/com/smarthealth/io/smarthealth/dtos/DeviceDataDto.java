package com.smarthealth.io.smarthealth.dtos;

import java.time.LocalDateTime;

public class DeviceDataDto {
    private String payloadId;
    private String deviceId;
    private int sequence;
    private String timeSerie;
    private String oximetryGraph;
    private String tempGraph;
    private String accelerationGraph;
    private LocalDateTime createdAt;

    public String getPayloadId() {
        return payloadId;
    }

    public void setPayloadId(String payloadId) {
        this.payloadId = payloadId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getTimeSerie() {
        return timeSerie;
    }

    public void setTimeSerie(String timeSerie) {
        this.timeSerie = timeSerie;
    }

    public String getOximetryGraph() {
        return oximetryGraph;
    }

    public void setOximetryGraph(String oximetryGraph) {
        this.oximetryGraph = oximetryGraph;
    }

    public String getTempGraph() {
        return tempGraph;
    }

    public void setTempGraph(String tempGraph) {
        this.tempGraph = tempGraph;
    }

    public String getAccelerationGraph() {
        return accelerationGraph;
    }

    public void setAccelerationGraph(String accelerationGraph) {
        this.accelerationGraph = accelerationGraph;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
