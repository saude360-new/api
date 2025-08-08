package com.smarthealth.io.smarthealth.controllers;

import com.smarthealth.io.smarthealth.models.DeviceData;
import com.smarthealth.io.smarthealth.services.DeviceDataService;
import com.smarthealth.io.smarthealth.dtos.DeviceDataDto;
import com.smarthealth.io.smarthealth.dtos.DeviceDataCreateDto;
import com.smarthealth.io.smarthealth.mappers.DeviceDataMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/device-data")
public class DeviceDataController {

    private final DeviceDataService deviceDataService;

    public DeviceDataController(DeviceDataService deviceDataService) {
        this.deviceDataService = deviceDataService;
    }

    @PostMapping
    public ResponseEntity<DeviceDataDto> create(@RequestBody DeviceDataCreateDto dto) {
        DeviceData created = deviceDataService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(DeviceDataMapper.toDto(created));
    }

    @GetMapping
    public ResponseEntity<List<DeviceDataDto>> getAll() {
        List<DeviceDataDto> dtoList = deviceDataService.findAll().stream()
                .map(DeviceDataMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceDataDto> getById(@PathVariable String id) {
        return deviceDataService.findById(id)
                .map(DeviceDataMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-device/{deviceId}")
    public ResponseEntity<List<DeviceDataDto>> getByDeviceId(@PathVariable String deviceId) {
        List<DeviceDataDto> dtoList = deviceDataService.findByDeviceId(deviceId).stream()
                .map(DeviceDataMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtoList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        deviceDataService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
