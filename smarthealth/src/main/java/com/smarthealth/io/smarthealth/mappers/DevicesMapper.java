package com.smarthealth.io.smarthealth.mappers;

import com.smarthealth.io.smarthealth.models.Devices;
import com.smarthealth.io.smarthealth.models.User;
import com.smarthealth.io.smarthealth.dtos.DevicesCreateDto;
import com.smarthealth.io.smarthealth.dtos.DevicesResponseDto;

import java.time.LocalDateTime;


public class DevicesMapper {

  public static Devices fromDto(DevicesCreateDto dto,User registeredBy){

    Devices devices = new Devices();


    devices.setBluetoothChipUid(dto.getBluetoothChipUid());
    devices.setSerialNumber(dto.getSerialNumber());
    devices.setRssi(dto.getRssi());
    devices.setBluetoothVersion(dto.getBluetoothVersion());
    devices.setFirmwareVersion(dto.getFirmwareVersion());
    devices.setCreatedAt(LocalDateTime.now());
    devices.setUpdatedAt(LocalDateTime.now());
    devices.setDeviceStatus("Dado completamente real");


    return devices;
  }
  

  public static DevicesResponseDto toResponse(Devices devices,){

    DevicesResponseDto dto = new DevicesResponseDto();

    dto.setDeviceId(null);
    dto.setDeviceStatus(null);
    dto.setLastConnected(null);
    dto.setRegisteredby(null);


    return dto;

  }
}
