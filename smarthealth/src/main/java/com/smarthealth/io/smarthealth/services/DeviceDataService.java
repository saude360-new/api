package com.smarthealth.io.smarthealth.services;

import com.smarthealth.io.smarthealth.dtos.DeviceDataCreateDto;
import com.smarthealth.io.smarthealth.exceptions.ResourceNotFoundException;
import com.smarthealth.io.smarthealth.mappers.DeviceDataMapper;
import com.smarthealth.io.smarthealth.models.DeviceData;
import com.smarthealth.io.smarthealth.models.Devices;
import com.smarthealth.io.smarthealth.repositories.DeviceDataRepository;
import com.smarthealth.io.smarthealth.repositories.DevicesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceDataService {

    private static final Logger logger = LoggerFactory.getLogger(DeviceDataService.class);

    private final DeviceDataRepository deviceDataRepository;
    private final DevicesRepository devicesRepository;
    private final DeviceDataMapper deviceDataMapper;

    @Autowired
    public DeviceDataService(DeviceDataRepository deviceDataRepository,
                             DevicesRepository devicesRepository,
                             DeviceDataMapper deviceDataMapper) {
        this.deviceDataRepository = deviceDataRepository;
        this.devicesRepository = devicesRepository;
        this.deviceDataMapper = deviceDataMapper;
    }

    public DeviceData create(DeviceDataCreateDto dto) {
        logger.info("Criando dados para dispositivo: {}", dto.getDeviceId());

        Devices device = devicesRepository.findById(dto.getDeviceId())
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo", dto.getDeviceId()));

        DeviceData entity = deviceDataMapper.fromCreateDto(dto, device);

        if (entity.getOximetry_graph() == null) entity.setOximetry_graph("{}");
        if (entity.getTemp_graph() == null) entity.setTemp_graph("{}");
        if (entity.getAcceleration_graph() == null) entity.setAcceleration_graph("{}");
        if (entity.getBpm_graph() == null) entity.setBpm_graph("{}");

        DeviceData saved = deviceDataRepository.save(entity);
        logger.info("Dados de dispositivo criados com sucesso. ID: {}", saved.getPayload_id());
        return saved;
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

    public String findOximetryGraphByDeviceId(String id) {
        return deviceDataRepository.getOximetryGraphWithDate(id);
    }

    public String findTempGraphByDeviceId(String id) {
        return deviceDataRepository.getTempGraphWithDate(id);
    }

    public String findAccelerationGraphByDeviceId(String id) {
        return deviceDataRepository.getAccelerationGraphWithDate(id);
    }

    public String findBpmGraphByDeviceId(String id) {
        return deviceDataRepository.getBpmGraphWithDate(id);
    }

    public void deleteById(String id) {
        if (!deviceDataRepository.existsById(id)) {
            throw new ResourceNotFoundException("Dados de dispositivo", id);
        }
        deviceDataRepository.deleteById(id);
    }
}
