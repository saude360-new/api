package com.smarthealth.io.smarthealth.controllers;

import com.smarthealth.io.smarthealth.models.Devices;
import com.smarthealth.io.smarthealth.models.User;
import com.smarthealth.io.smarthealth.services.DevicesService;
import com.smarthealth.io.smarthealth.dtos.DevicesCreateDto;
import com.smarthealth.io.smarthealth.dtos.DevicesResponseDto;
import com.smarthealth.io.smarthealth.dtos.UserCreateDto;
import com.smarthealth.io.smarthealth.dtos.UserResponseDto;
import com.smarthealth.io.smarthealth.mappers.DevicesMapper;
import com.smarthealth.io.smarthealth.mappers.UserMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController

@RequestMapping("/devices")
public class DevicesController {

  private final DevicesService devicesService;

 
    public DevicesController(DevicesService devicesService ) {
        this.devicesService = devicesService;
    }


    @PostMapping 
    public ResponseEntity<DevicesResponseDto> create(@RequestBody DevicesCreateDto devices) { 
        Devices created = devicesService.create(devices);
       return ResponseEntity.ok(DevicesMapper.toResponse(created));
    }


    @GetMapping 
    public ResponseEntity<List<DevicesResponseDto>> getAll() {
    List<Devices> devices = devicesService.findAll();
    List<DevicesResponseDto> dtoList = devices.stream()
    .map(DevicesMapper::toResponse) 
            .toList();
    return ResponseEntity.ok(dtoList);
    }


    @GetMapping("/{id}")
    public ResponseEntity<DevicesResponseDto> getById(@PathVariable String id) {

      Optional<Devices> optionalDevices = devicesService.findById(id);
      return optionalDevices
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
