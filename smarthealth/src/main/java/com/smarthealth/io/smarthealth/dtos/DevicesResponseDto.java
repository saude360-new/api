package com.smarthealth.io.smarthealth.dtos;

import java.time.LocalDateTime;

import com.smarthealth.io.smarthealth.models.Devices;

public class DevicesResponseDto {

  private String deviceId;
  private Devices.Status deviceStatus;
  private LocalDateTime lastConnected;
  private String registeredby;

  
  public String getDeviceId() {
    return deviceId;
  }
  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }
  public Devices.Status getDeviceStatus() {
    return deviceStatus;
  }
  public void setDeviceStatus(Devices.Status deviceStatus) {
    this.deviceStatus = deviceStatus;
  }
  public LocalDateTime getLastConnected() {
    return lastConnected;
  }
  public void setLastConnected(LocalDateTime lastConnected) {
    this.lastConnected = lastConnected;
  }
  public String getRegisteredby() {
    return registeredby;
  }
  public void setRegisteredby(String registeredby) {
    this.registeredby = registeredby;
  }
  
  
}
