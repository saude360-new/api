package com.smarthealth.io.smarthealth.services;

import com.smarthealth.io.smarthealth.dtos.DevicesCreateDto;
import com.smarthealth.io.smarthealth.exceptions.ResourceNotFoundException;
import com.smarthealth.io.smarthealth.mappers.DevicesMapper;
import com.smarthealth.io.smarthealth.models.Devices;
import com.smarthealth.io.smarthealth.models.User;
import com.smarthealth.io.smarthealth.repositories.DevicesRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Serviço para gerenciamento de dispositivos.
 */
@Service
public class DevicesService {

    private static final Logger logger = LoggerFactory.getLogger(DevicesService.class);

    private final DevicesRepository devicesRepository;
    private final UserService userService;
    private final DevicesMapper devicesMapper;

    @Autowired
    public DevicesService(DevicesRepository devicesRepository, 
                         UserService userService,
                         DevicesMapper devicesMapper) {
        this.devicesRepository = devicesRepository;
        this.userService = userService;
        this.devicesMapper = devicesMapper;
    }

    /**
     * Cria um novo dispositivo.
     */
    public Devices create(DevicesCreateDto dto) {
        logger.info("Criando dispositivo para usuário: {}", dto.getRegisteredBy());
        
        User user = userService.findById(dto.getRegisteredBy())
            .orElseThrow(() -> new ResourceNotFoundException("Usuário", dto.getRegisteredBy()));

        Devices device = devicesMapper.fromDto(dto, user);
        Devices savedDevice = devicesRepository.save(device);
        
        logger.info("Dispositivo criado com sucesso. ID: {}", savedDevice.getDeviceId());
        return savedDevice;
    }

    /**
     * Busca todos os dispositivos.
     */
    public List<Devices> findAll() {
        logger.debug("Buscando todos os dispositivos");
        return devicesRepository.findAll();
    }

    /**
     * Busca um dispositivo por ID.
     */
    public Optional<Devices> findById(String id) {
        logger.debug("Buscando dispositivo por ID: {}", id);
        return devicesRepository.findById(id);
    }

    /**
     * Busca um dispositivo por UID do chip Bluetooth.
     */
    public Optional<Devices> findByBluetoothChipUid(String bluetoothChipUid) {
        logger.debug("Buscando dispositivo por Bluetooth UID: {}", bluetoothChipUid);
        return devicesRepository.findByBluetoothChipUid(bluetoothChipUid);
    }

    /**
     * Busca dispositivo por usuário que o registrou.
     */
    public Optional<Devices> findByRegisteredBy(String userId) {
        logger.debug("Buscando dispositivos registrados pelo usuário: {}", userId);
        return devicesRepository.findByRegisteredByUserId(userId);
    }

    /**
     * Remove um dispositivo por ID.
     */
    public void deleteById(String id) {
        logger.info("Removendo dispositivo com ID: {}", id);
        
        if (!devicesRepository.existsById(id)) {
            throw new ResourceNotFoundException("Dispositivo", id);
        }
        
        devicesRepository.deleteById(id);
        logger.info("Dispositivo removido com sucesso. ID: {}", id);
    }
}

