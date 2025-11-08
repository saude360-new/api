package com.smarthealth.io.smarthealth.mappers;

import com.smarthealth.io.smarthealth.dtos.FallDataDTO;
import com.smarthealth.io.smarthealth.models.FallData;
import com.smarthealth.io.smarthealth.models.Devices;

public class FallDataMapper {

    public static FallDataDTO toDTO(FallData fallData) {
        FallDataDTO dto = new FallDataDTO();
        dto.setPayload_id(fallData.getPayload_id());
        dto.setDevice_id(fallData.getDevices() != null ? fallData.getDevices().getDeviceId() : null);
        dto.setSequence(fallData.getSequence());
        dto.setIntensity(fallData.getIntensity());
        dto.setCreated_at(fallData.getCreated_at());
        return dto;
    }

    public static FallData toEntity(FallDataDTO dto, Devices device) {
        FallData fallData = new FallData();
        fallData.setPayload_id(dto.getPayload_id());
        fallData.setDevices(device);
        fallData.setSequence(dto.getSequence());
        fallData.setIntensity(dto.getIntensity());
        fallData.setCreated_at(dto.getCreated_at());
        return fallData;
    }
}
