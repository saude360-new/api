package com.smarthealth.io.smarthealth.dtos;

public class DevicesResponseDto {

  private String deviceId;
  private String deviceStatus;
  private String lastConnected;
  private String registeredby;

  
  public String getDeviceId() {
    return deviceId;
  }
  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }
  public String getDeviceStatus() {
    return deviceStatus;
  }
  public void setDeviceStatus(String deviceStatus) {
    this.deviceStatus = deviceStatus;
  }
  public String getLastConnected() {
    return lastConnected;
  }
  public void setLastConnected(String lastConnected) {
    this.lastConnected = lastConnected;
  }
  public String getRegisteredby() {
    return registeredby;
  }
  public void setRegisteredby(String registeredby) {
    this.registeredby = registeredby;
  }
  
  
}
