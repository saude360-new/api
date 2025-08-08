package com.smarthealth.io.smarthealth.services;

import com.smarthealth.io.smarthealth.models.DeviceData;
import com.smarthealth.io.smarthealth.models.Devices;
import com.smarthealth.io.smarthealth.repositories.*;
import com.smarthealth.io.smarthealth.repositories.DevicesRepository;
import com.smarthealth.io.smarthealth.dtos.DeviceDataCreateDto;
import com.smarthealth.io.smarthealth.mappers.DeviceDataMapper;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceDataService {

    private final DeviceDataRepository deviceDataRepository;
    private final DevicesRepository devicesRepository;

    public DeviceDataService(DeviceDataRepository deviceDataRepository, DevicesRepository devicesRepository) {
        this.deviceDataRepository = deviceDataRepository;
        this.devicesRepository = devicesRepository;
    }

    public DeviceData create(DeviceDataCreateDto dto) {
        Devices device = devicesRepository.findById(dto.getDeviceId())
                .orElseThrow(() -> new RuntimeException("Dispositivo não encontrado: " + dto.getDeviceId()));
        DeviceData entity = DeviceDataMapper.fromCreateDto(dto, device);
        return deviceDataRepository.save(entity);
    }

    public List<DeviceData> findAll() {
        return deviceDataRepository.findAll();
    }

    public Optional<DeviceData> findById(String id) {
        return deviceDataRepository.findById(id);
    }

    public List<DeviceData> findByDeviceId(String deviceId) {
        return deviceDataRepository.findByDevices_DeviceId(deviceId);
    }

    public void deleteById(String id) {
        deviceDataRepository.deleteById(id);
    }
}
