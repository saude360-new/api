package com.smarthealth.io.smarthealth.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

@Entity
@Table(name = "devices")
public class Devices{

  public enum Status{
    pairing,
    connected, 
    disconnected
  }

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "device_id")
  private String deviceId;

  @Column(name = "bluetooth_chip_uid", nullable = false, unique = true)
  private String bluetoothChipUid;
  
  @Column(name = "serial_number", nullable = false, unique = true)
  private String serialNumber;

  @Column(name = "bluetooth_version", nullable = false)
  private String bluetoothVersion;

  @Column(name = "last_connected", nullable = true)
  private LocalDateTime lastConnected;

  @Column(name = "rssi", nullable = false, columnDefinition = "INT DEFAULT 0")
  private int rssi;

    //criar o tipo dps e mudar este String junto com as get e set desta variavel

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  @JdbcType(PostgreSQLEnumJdbcType.class)
  private Status deviceStatus;

  @Column(name = "pairing_key", nullable = false, unique = true)
  private String pairingKey;

  @Column(name = "firmware_version", nullable = false)
  private String firmwareVersion;

  @Column(name = "last_error")
  private String lastError;

  @Column(name = "connection_error")
  private String connectionError;

  @ManyToOne
    @JoinColumn(name = "registered_by", unique = true, nullable = false)
    private User registeredBy;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  public Devices(){}

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

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

  public String getBluetoothVersion() {
    return bluetoothVersion;
  }

  public void setBluetoothVersion(String bluetoothVersion) {
    this.bluetoothVersion = bluetoothVersion;
  }

  public LocalDateTime getLastConnected() {
    return lastConnected;
  }

  public void setLastConnected(LocalDateTime lastConnected) {
    this.lastConnected = lastConnected;
  }

  public int getRssi() {
    return rssi;
  }

  public void setRssi(int rssi) {
    this.rssi = rssi;
  }

  public Status getDeviceStatus() {
    return deviceStatus;
  }

  public void setDeviceStatus(Status deviceStatus) {
    this.deviceStatus = deviceStatus;
  }

  public String getPairingKey() {
    return pairingKey;
  }

  public void setPairingKey(String pairingKey) {
    this.pairingKey = pairingKey;
  }

  public String getFirmwareVersion() {
    return firmwareVersion;
  }

  public void setFirmwareVersion(String firmwareVersion) {
    this.firmwareVersion = firmwareVersion;
  }

  public String getLastError() {
    return lastError;
  }

  public void setLastError(String lastError) {
    this.lastError = lastError;
  }

  public String getConnectionError() {
    return connectionError;
  }

  public void setConnectionError(String connectionError) {
    this.connectionError = connectionError;
  }

  public User getRegisteredBy() {
    return registeredBy;
  }

  public void setRegisteredBy(User registeredBy) {
    this.registeredBy = registeredBy;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  
  

 
}
