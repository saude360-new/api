package com.smarthealth.io.smarthealth.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "devices")
public class Devices{

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "device_id")
  private String device_id;

  @Column(name = "bluetooth_chip_uid", nullable = false, unique = true)
  private String bluetooth_chip_uid;
  
  @Column(name = "serial_number", nullable = false, unique = true)
  private String serial_number;

  @Column(name = "bluetooth_version", nullable = false)
  private String bluetooth_version;

  @Column(name = "last_connected")
  private LocalDateTime last_connected;

  @Column(name = "rssi", nullable = false, columnDefinition = "INT DEFAULT 0")
  private int rssi;

    //criar o tipo dps e mudar este String junto com as get e set desta variavel
  @Column(name = "device_status", nullable = false)
  private String device_status;

  @Column(name = "pairing_key", nullable = false, unique = true)
  private String pairing_key;

  @Column(name = "firmware_version", nullable = false)
  private String firmware_version;

  @Column(name = "last_error")
  private String last_error;

  @Column(name = "connection_error")
  private String connection_error;

  @ManyToOne
    @JoinColumn(name = "registered_by", unique = true, nullable = false)
    private User user;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime created_at;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updated_at;

  public Devices(){}

  public String getDevice_id() {
    return device_id;
  }

  public void setDevice_id(String device_id) {
    this.device_id = device_id;
  }

  public String getBluetooth_chip_uid() {
    return bluetooth_chip_uid;
  }

  public void setBluetooth_chip_uid(String bluetooth_chip_uid) {
    this.bluetooth_chip_uid = bluetooth_chip_uid;
  }

  public String getSerial_number() {
    return serial_number;
  }

  public void setSerial_number(String serial_number) {
    this.serial_number = serial_number;
  }

  public String getBluetooth_version() {
    return bluetooth_version;
  }

  public void setBluetooth_version(String bluetooth_version) {
    this.bluetooth_version = bluetooth_version;
  }

  public LocalDateTime getLast_connected() {
    return last_connected;
  }

  public void setLast_connected(LocalDateTime last_connected) {
    this.last_connected = last_connected;
  }

  public int getRssi() {
    return rssi;
  }

  public void setRssi(int rssi) {
    this.rssi = rssi;
  }

  public String getDevice_status() {
    return device_status;
  }

  public void setDevice_status(String device_status) {
    this.device_status = device_status;
  }

  public String getPairing_key() {
    return pairing_key;
  }

  public void setPairing_key(String pairing_key) {
    this.pairing_key = pairing_key;
  }

  public String getFirmware_version() {
    return firmware_version;
  }

  public void setFirmware_version(String firmware_version) {
    this.firmware_version = firmware_version;
  }

  public String getLast_error() {
    return last_error;
  }

  public void setLast_error(String last_error) {
    this.last_error = last_error;
  }

  public String getConnection_error() {
    return connection_error;
  }

  public void setConnection_error(String connection_error) {
    this.connection_error = connection_error;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public LocalDateTime getCreated_at() {
    return created_at;
  }

  public void setCreated_at(LocalDateTime created_at) {
    this.created_at = created_at;
  }

  public LocalDateTime getUpdated_at() {
    return updated_at;
  }

  public void setUpdated_at(LocalDateTime updated_at) {
    this.updated_at = updated_at;
  }




}
