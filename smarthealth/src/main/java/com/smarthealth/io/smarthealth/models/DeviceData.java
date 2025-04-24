package com.smarthealth.io.smarthealth.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "device_data")
public class DeviceData {

  @Id
  @GeneratedValue( strategy = GenerationType.UUID)
      @Column(name = "payload_id")
      private String payload_id;
  
  @ManyToOne
  @JoinColumn(name = "device_id", nullable = false, unique = true)
  private Devices devices;

  
    @Column(name = "sequence", nullable = false,updatable = false, insertable = false)
    private int sequence;

  public int getSequence() {
    return sequence;
  }

  public void setSequence(int sequence) {
    this.sequence = sequence;
  }

  @Column(name = "time_serie",nullable = false)
  private String time_serie;

  @Column(name = "oximetry_graph",nullable = false, columnDefinition = "json")
  private String oximetry_graph;

  @Column(name = "temp_graph",nullable = false, columnDefinition = "json")
  private String temp_graph;

  @Column(name = "acceleration_graph",nullable = false, columnDefinition = "json")
  private String acceleration_graph;

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

  public String getTime_serie() {
    return time_serie;
  }

  public void setTime_serie(String time_serie) {
    this.time_serie = time_serie;
  }

  public String getOximetry_graph() {
    return oximetry_graph;
  }

  public void setOximetry_graph(String oximetry_graph) {
    this.oximetry_graph = oximetry_graph;
  }

  public String getTemp_graph() {
    return temp_graph;
  }

  public void setTemp_graph(String temp_graph) {
    this.temp_graph = temp_graph;
  }

  public String getAcceleration_graph() {
    return acceleration_graph;
  }

  public void setAcceleration_graph(String acceleration_graph) {
    this.acceleration_graph = acceleration_graph;
  }

  public LocalDateTime getCreated_at() {
    return created_at;
  }

  public void setCreated_at(LocalDateTime created_at) {
    this.created_at = created_at;
  }



  
  
}
