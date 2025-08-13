package com.smarthealth.io.smarthealth.mappers;

import com.smarthealth.io.smarthealth.models.DeviceData;
import com.smarthealth.io.smarthealth.models.Devices;
import com.smarthealth.io.smarthealth.dtos.DeviceDataDto;
import com.smarthealth.io.smarthealth.dtos.DeviceDataCreateDto;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Mapper para conversão entre entidades DeviceData e DTOs.
 */
@Component
public class DeviceDataMapper {

    /**
     * Converte uma entidade DeviceData para DTO.
     */
    public DeviceDataDto toDto(DeviceData deviceData) {
        if (deviceData == null) {
            return null;
        }
        
        DeviceDataDto dto = new DeviceDataDto();
        dto.setPayloadId(deviceData.getPayload_id());
        dto.setDeviceId(deviceData.getDevices().getDeviceId());
        dto.setSequence(deviceData.getSequence());
        dto.setTimeSerie(deviceData.getTime_serie());
        dto.setOximetryGraph(deviceData.getOximetry_graph());
        dto.setTempGraph(deviceData.getTemp_graph());
        dto.setAccelerationGraph(deviceData.getAcceleration_graph());
        dto.setCreatedAt(deviceData.getCreated_at());
        return dto;
    }

    /**
     * Converte um DTO de criação para entidade DeviceData.
     */
    public DeviceData fromCreateDto(DeviceDataCreateDto dto, Devices device) {
        if (dto == null) {
            return null;
        }
        
        DeviceData entity = new DeviceData();
        entity.setDevices(device);
        entity.setTime_serie(dto.getTimeSerie());
        entity.setOximetry_graph(dto.getOximetryGraph());
        entity.setTemp_graph(dto.getTempGraph());
        entity.setAcceleration_graph(dto.getAccelerationGraph());
        entity.setCreated_at(LocalDateTime.now());
        return entity;
    }
}

