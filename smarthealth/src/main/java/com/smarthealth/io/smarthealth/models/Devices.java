package com.smarthealth.io.smarthealth.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "devices")
public class Devices{

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

  @Column(name = "last_connected")
  private LocalDateTime lastConnected;

  @Column(name = "rssi", nullable = false, columnDefinition = "INT DEFAULT 0")
  private int rssi;

    //criar o tipo dps e mudar este String junto com as get e set desta variavel
  @Column(name = "device_status", nullable = false)
  private String deviceStatus;

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
    private User user;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  public Devices(){}

  


}
