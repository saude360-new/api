package com.smarthealth.io.smarthealth.mappers;

import com.smarthealth.io.smarthealth.models.DeviceData;
import com.smarthealth.io.smarthealth.models.Devices;
import com.smarthealth.io.smarthealth.dtos.DeviceDataDto;
import com.smarthealth.io.smarthealth.dtos.DeviceDataCreateDto;

import java.time.LocalDateTime;

public class DeviceDataMapper {

    public static DeviceDataDto toDto(DeviceData deviceData) {
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

    public static DeviceData fromCreateDto(DeviceDataCreateDto dto, Devices device) {
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
