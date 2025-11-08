package com.smarthealth.io.smarthealth.dtos;

import java.time.LocalDateTime;

public class FallDataDTO {
    private String payload_id;
    private String device_id;
    private int sequence;
    private String intensity;
    private LocalDateTime created_at;

    public String getPayload_id() {
        return payload_id;
    }

    public void setPayload_id(String payload_id) {
        this.payload_id = payload_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getIntensity() {
        return intensity;
    }

    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
