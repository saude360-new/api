package com.smarthealth.io.smarthealth.controllers;

import com.smarthealth.io.smarthealth.dtos.DeviceDataCreateDto;
import com.smarthealth.io.smarthealth.dtos.DeviceDataDto;
import com.smarthealth.io.smarthealth.exceptions.ResourceNotFoundException;
import com.smarthealth.io.smarthealth.mappers.DeviceDataMapper;
import com.smarthealth.io.smarthealth.models.DeviceData;
import com.smarthealth.io.smarthealth.services.DeviceDataService;
import com.smarthealth.io.smarthealth.services.DevicesService;


import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gerenciamento de dados de dispositivos.
 */
@RestController
@RequestMapping("/device-data")
public class DeviceDataController {

    private static final Logger logger = LoggerFactory.getLogger(DeviceDataController.class);

    private final DeviceDataService deviceDataService;
    private final DeviceDataMapper deviceDataMapper;
    private final DevicesService devicesService;

    @Autowired
    public DeviceDataController(DeviceDataService deviceDataService, DeviceDataMapper deviceDataMapper,DevicesService devicesService) {
        this.deviceDataService = deviceDataService;
        this.deviceDataMapper = deviceDataMapper;
        this.devicesService = devicesService;
    }

    public void checkDeviceID(String deviceId){

      devicesService.findById(deviceId).orElseThrow(() -> new ResourceNotFoundException("Dispositivo: {}", deviceId));
    }

    /**
     * Cria novos dados de dispositivo.
     */
    @PostMapping
    public ResponseEntity<DeviceDataDto> create(@Valid @RequestBody DeviceDataCreateDto dto) {
        logger.info("Recebida requisição para criar dados do dispositivo: {}", dto.getDeviceId());
        
        DeviceData created = deviceDataService.create(dto);
        DeviceDataDto response = deviceDataMapper.toDto(created);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Busca todos os dados de dispositivos.
     */
    @GetMapping
    public ResponseEntity<List<DeviceDataDto>> getAll() {
        logger.debug("Recebida requisição para buscar todos os dados de dispositivos");
        
        List<DeviceDataDto> dtoList = deviceDataService.findAll().stream()
                .map(deviceDataMapper::toDto)
                .toList();
        
        return ResponseEntity.ok(dtoList);
    }

    /**
     * Busca dados de dispositivo por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DeviceDataDto> getById(@PathVariable String id) {
        logger.debug("Recebida requisição para buscar dados de dispositivo com ID: {}", id);
        
        DeviceData deviceData = deviceDataService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dados de dispositivo", id));
        
        DeviceDataDto response = deviceDataMapper.toDto(deviceData);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca dados por ID do dispositivo.
     */
    @GetMapping("/by-device/{deviceId}")
    public ResponseEntity<List<DeviceDataDto>> getByDeviceId(@PathVariable String deviceId) {
        logger.debug("Recebida requisição para buscar dados do dispositivo: {}", deviceId);

        checkDeviceID(deviceId);
        
        List<DeviceDataDto> dtoList = deviceDataService.findByDeviceId(deviceId).stream()
                .map(deviceDataMapper::toDto)
                .toList();
        
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/by-device/{deviceId}/oximetry")
    public ResponseEntity<String> getByDeviceIdOximetry(@PathVariable String deviceId) {
        logger.debug("Recebida requisição para buscar dados de oximetro do dispositivo: {}", deviceId);
        
        checkDeviceID(deviceId);

        String dado = deviceDataService.findOximetryGraphByDeviceId(deviceId);

        if (dado == null){
          dado = "[]";
        }
        
        return ResponseEntity.ok(dado);
    }

    @GetMapping("/by-device/{deviceId}/temp")
    public ResponseEntity<String> getByDeviceIdTemp(@PathVariable String deviceId) {
        logger.debug("Recebida requisição para buscar dados de temperatura do dispositivo: {}", deviceId);
        
        checkDeviceID(deviceId);

        String dado = deviceDataService.findTempGraphByDeviceId(deviceId);

        if (dado == null){
          dado = "[]";
        }
        
        return ResponseEntity.ok(dado);
    }

    @GetMapping("/by-device/{deviceId}/acceleration")
    public ResponseEntity<String> getByDeviceIdAcceleration(@PathVariable String deviceId) {
        logger.debug("Recebida requisição para buscar dados de aceleracao do dispositivo: {}", deviceId);
        
        checkDeviceID(deviceId);
        
        String dado = deviceDataService.findAccelerationGraphByDeviceId(deviceId);

        if (dado == null){
          dado = "[]";
        }
        
        return ResponseEntity.ok(dado);
    }

    /**
     * Remove dados de dispositivo por ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        logger.info("Recebida requisição para remover dados de dispositivo com ID: {}", id);
        
        deviceDataService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

