package com.smarthealth.io.smarthealth.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "data_exchange_logs")
public class DataExchangeLogs {
  @Id
  @GeneratedValue( strategy = GenerationType.UUID)
      @Column(name = "log_id")
      private String log_id;
  

  @ManyToOne
  @JoinColumn(name = "device_id", nullable = false, unique = true)
  private Devices devices;

  @Column(name = "data_sent")
  private String data_sent;

  @Column(name = "data_received")
  private String data_received;

  @Column(name = "transport_props",nullable = false, columnDefinition = "jsonb")
  private String transport_props;

  @Column(name = "created_at",nullable = false)
  private LocalDateTime created_at;

  public String getLog_id() {
    return log_id;
  }

  public void setLog_id(String log_id) {
    this.log_id = log_id;
  }

  public Devices getDevices() {
    return devices;
  }

  public void setDevices(Devices devices) {
    this.devices = devices;
  }

  public String getData_sent() {
    return data_sent;
  }

  public void setData_sent(String data_sent) {
    this.data_sent = data_sent;
  }

  public String getData_received() {
    return data_received;
  }

  public void setData_received(String data_received) {
    this.data_received = data_received;
  }

  public String getTransport_props() {
    return transport_props;
  }

  public void setTransport_props(String transport_props) {
    this.transport_props = transport_props;
  }

  public LocalDateTime getCreated_at() {
    return created_at;
  }

  public void setCreated_at(LocalDateTime created_at) {
    this.created_at = created_at;
  }

  
  
}
