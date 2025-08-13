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

/**
 * Serviço para gerenciamento de dados de dispositivos.
 */
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

    /**
     * Cria novos dados de dispositivo.
     */
    public DeviceData create(DeviceDataCreateDto dto) {
        logger.info("Criando dados para dispositivo: {}", dto.getDeviceId());
        
        Devices device = devicesRepository.findById(dto.getDeviceId())
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo", dto.getDeviceId()));
        
        DeviceData entity = deviceDataMapper.fromCreateDto(dto, device);
        DeviceData savedEntity = deviceDataRepository.save(entity);
        
        logger.info("Dados de dispositivo criados com sucesso. ID: {}", savedEntity.getPayload_id());
        return savedEntity;
    }

    /**
     * Busca todos os dados de dispositivos.
     */
    public List<DeviceData> findAll() {
        logger.debug("Buscando todos os dados de dispositivos");
        return deviceDataRepository.findAll();
    }

    /**
     * Busca dados de dispositivo por ID.
     */
    public Optional<DeviceData> findById(String id) {
        logger.debug("Buscando dados de dispositivo por ID: {}", id);
        return deviceDataRepository.findById(id);
    }

    /**
     * Busca dados por ID do dispositivo.
     */
    public List<DeviceData> findByDeviceId(String deviceId) {
        logger.debug("Buscando dados para dispositivo: {}", deviceId);
        return deviceDataRepository.findByDevices_DeviceId(deviceId);
    }

    /**
     * Remove dados de dispositivo por ID.
     */
    public void deleteById(String id) {
        logger.info("Removendo dados de dispositivo com ID: {}", id);
        
        if (!deviceDataRepository.existsById(id)) {
            throw new ResourceNotFoundException("Dados de dispositivo", id);
        }
        
        deviceDataRepository.deleteById(id);
        logger.info("Dados de dispositivo removidos com sucesso. ID: {}", id);
    }
}

