package com.smarthealth.io.smarthealth.dtos;

public class DeviceDataCreateDto {
    private String deviceId;
    private String timeSerie;
    private String oximetryGraph;
    private String tempGraph;
    private String accelerationGraph;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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
}
