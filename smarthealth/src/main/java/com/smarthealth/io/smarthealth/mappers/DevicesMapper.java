package com.smarthealth.io.smarthealth.mappers;

import java.time.LocalDateTime;

import com.smarthealth.io.smarthealth.dtos.DevicesCreateDto;
import com.smarthealth.io.smarthealth.dtos.DevicesResponseDto;
import com.smarthealth.io.smarthealth.models.Devices;
import com.smarthealth.io.smarthealth.models.User;
import com.smarthealth.io.smarthealth.services.UserService;


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
  

  public static DevicesResponseDto toResponse(Devices devices){

    DevicesResponseDto dto = new DevicesResponseDto();

    

    dto.setDeviceId(devices.getDeviceId());
    dto.setDeviceStatus(devices.getDeviceStatus());
    dto.setLastConnected(devices.getLastConnected());
    dto.setRegisteredby(devices.getRegisteredBy().getUserId());


    return dto;
  

  }
}
