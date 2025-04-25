package com.smarthealth.io.smarthealth.dtos;

import jakarta.validation.constraints.*;


public class DevicesCreateDto {

  @NotBlank
  private String bluetoothChipUid;

  @NotBlank
  private String serialNumber;

  @NotBlank
  private int rssi;

  @NotBlank
  private String bluetoothVersion;

  @NotBlank
  private String registeredBy;

  @NotBlank
  private String firmwareVersion;

  @NotBlank
  private String pairingKey;

  public String getBluetoothChipUid() {
    return bluetoothChipUid;
  }

  public void setBluetoothChipUid(String bluetoothChipUid) {
    this.bluetoothChipUid = bluetoothChipUid;
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public int getRssi() {
    return rssi;
  }

  public void setRssi(int rssi) {
    this.rssi = rssi;
  }

  public String getBluetoothVersion() {
    return bluetoothVersion;
  }

  public void setBluetoothVersion(String bluetoothVersion) {
    this.bluetoothVersion = bluetoothVersion;
  }

  public String getRegisteredBy() {
    return registeredBy;
  }

  public void setRegisteredBy(String registeredBy) {
    this.registeredBy = registeredBy;
  }

  public String getFirmwareVersion() {
    return firmwareVersion;
  }

  public void setFirmwareVersion(String firmwareVersion) {
    this.firmwareVersion = firmwareVersion;
  }

  public String getPairingKey() {
    return pairingKey;
  }

  public void setPairingKey(String pairingKey) {
    this.pairingKey = pairingKey;
  }

  


  

}
