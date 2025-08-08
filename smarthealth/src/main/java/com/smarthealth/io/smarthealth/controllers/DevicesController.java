package com.smarthealth.io.smarthealth.controllers;

import com.smarthealth.io.smarthealth.models.Devices;
import com.smarthealth.io.smarthealth.services.DevicesService;
import com.smarthealth.io.smarthealth.dtos.DevicesCreateDto;
import com.smarthealth.io.smarthealth.dtos.DevicesResponseDto;
import com.smarthealth.io.smarthealth.mappers.DevicesMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devices")
public class DevicesController {

    private final DevicesService devicesService;

    public DevicesController(DevicesService devicesService) {
        this.devicesService = devicesService;
    }

    @PostMapping
    public ResponseEntity<DevicesResponseDto> create(@RequestBody DevicesCreateDto devicesDto) {
        Devices created = devicesService.create(devicesDto);
        DevicesResponseDto response = DevicesMapper.toResponse(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<DevicesResponseDto>> getAll() {
        List<Devices> devicesList = devicesService.findAll();
        List<DevicesResponseDto> dtoList = devicesList.stream()
                .map(DevicesMapper::toResponse)
                .toList();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DevicesResponseDto> getById(@PathVariable String id) {
        return devicesService.findById(id)
                .map(DevicesMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        devicesService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
