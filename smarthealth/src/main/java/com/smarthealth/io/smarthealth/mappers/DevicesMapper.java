package com.smarthealth.io.smarthealth.mappers;

import com.smarthealth.io.smarthealth.dtos.DevicesCreateDto;
import com.smarthealth.io.smarthealth.dtos.DevicesResponseDto;
import com.smarthealth.io.smarthealth.models.Devices;
import com.smarthealth.io.smarthealth.models.User;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Mapper para conversão entre entidades Devices e DTOs.
 */
@Component
public class DevicesMapper {

    /**
     * Converte um DTO de criação para entidade Devices.
     */
    public Devices fromDto(DevicesCreateDto dto, User registeredBy) {
        if (dto == null) {
            return null;
        }

        Devices devices = new Devices();
        devices.setBluetoothChipUid(dto.getBluetoothChipUid());
        devices.setSerialNumber(dto.getSerialNumber());
        devices.setRssi(dto.getRssi());
        devices.setBluetoothVersion(dto.getBluetoothVersion());
        devices.setFirmwareVersion(dto.getFirmwareVersion());
        devices.setCreatedAt(LocalDateTime.now());
        devices.setUpdatedAt(LocalDateTime.now());
        devices.setDeviceStatus(dto.getDeviceStatus());
        devices.setPairingKey(dto.getPairingKey());
        devices.setRegisteredBy(registeredBy);

        return devices;
    }

    /**
     * Converte uma entidade Devices para DTO de resposta.
     */
    public DevicesResponseDto toResponse(Devices devices) {
        if (devices == null) {
            return null;
        }

        DevicesResponseDto dto = new DevicesResponseDto();
        dto.setDeviceId(devices.getDeviceId());
        dto.setDeviceStatus(devices.getDeviceStatus());
        dto.setLastConnected(devices.getLastConnected());
        dto.setRegisteredby(devices.getRegisteredBy().getUserId());

        return dto;
    }
}

