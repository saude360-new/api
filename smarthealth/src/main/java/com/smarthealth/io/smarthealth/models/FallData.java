package com.smarthealth.io.smarthealth.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "fall_data")
public class FallData {

  @Id
  @GeneratedValue( strategy = GenerationType.UUID)
      @Column(name = "payload_id")
      private String payload_id;
  
  @ManyToOne
  @JoinColumn(name = "device_id", nullable = false, unique = true)
  private Devices devices;

  
    @Column(name = "sequence", nullable = false)
    private int sequence;
 
  @Column(name = "intensity",nullable = false)
  private String intensity;

  @Column(name = "created_at",nullable = false)
  private LocalDateTime created_at;

  public String getPayload_id() {
    return payload_id;
  }

  public void setPayload_id(String payload_id) {
    this.payload_id = payload_id;
  }

  public Devices getDevices() {
    return devices;
  }

  public void setDevices(Devices devices) {
    this.devices = devices;
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
