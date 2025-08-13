package com.smarthealth.io.smarthealth.controllers;

import com.smarthealth.io.smarthealth.dtos.DevicesCreateDto;
import com.smarthealth.io.smarthealth.dtos.DevicesResponseDto;
import com.smarthealth.io.smarthealth.exceptions.ResourceNotFoundException;
import com.smarthealth.io.smarthealth.mappers.DevicesMapper;
import com.smarthealth.io.smarthealth.models.Devices;
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
 * Controller REST para gerenciamento de dispositivos.
 */
@RestController
@RequestMapping("/devices")
public class DevicesController {

    private static final Logger logger = LoggerFactory.getLogger(DevicesController.class);

    private final DevicesService devicesService;
    private final DevicesMapper devicesMapper;

    @Autowired
    public DevicesController(DevicesService devicesService, DevicesMapper devicesMapper) {
        this.devicesService = devicesService;
        this.devicesMapper = devicesMapper;
    }

    /**
     * Cria um novo dispositivo.
     */
    @PostMapping
    public ResponseEntity<DevicesResponseDto> create(@Valid @RequestBody DevicesCreateDto devicesDto) {
        logger.info("Recebida requisição para criar dispositivo para usuário: {}", devicesDto.getRegisteredBy());
        
        Devices created = devicesService.create(devicesDto);
        DevicesResponseDto response = devicesMapper.toResponse(created);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Busca todos os dispositivos.
     */
    @GetMapping
    public ResponseEntity<List<DevicesResponseDto>> getAll() {
        logger.debug("Recebida requisição para buscar todos os dispositivos");
        
        List<Devices> devicesList = devicesService.findAll();
        List<DevicesResponseDto> dtoList = devicesList.stream()
                .map(devicesMapper::toResponse)
                .toList();
        
        return ResponseEntity.ok(dtoList);
    }

    /**
     * Busca um dispositivo por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DevicesResponseDto> getById(@PathVariable String id) {
        logger.debug("Recebida requisição para buscar dispositivo com ID: {}", id);
        
        Devices device = devicesService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo", id));
        
        DevicesResponseDto response = devicesMapper.toResponse(device);
        return ResponseEntity.ok(response);
    }

    /**
     * Remove um dispositivo por ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        logger.info("Recebida requisição para remover dispositivo com ID: {}", id);
        
        devicesService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

