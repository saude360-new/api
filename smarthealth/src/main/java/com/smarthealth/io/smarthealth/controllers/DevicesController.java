package com.smarthealth.io.smarthealth.controllers;

import com.smarthealth.io.smarthealth.models.Devices;
import com.smarthealth.io.smarthealth.services.DevicesService;
//import com.smarthealth.io.smarthealth.dtos.UserCreateDto;
//import com.smarthealth.io.smarthealth.dtos.UserResponseDto;
import com.smarthealth.io.smarthealth.mappers.DevicesMapper;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController

@RequestMapping("/devices")
public class DevicesController {

  private final DevicesService devicesService;
    private final DevicesMapper devicesMapper;
 
    public DevicesController(DevicesService devicesService , DevicesMapper devicesMapper) {
        this.devicesService = devicesService;
        this.devicesMapper = devicesMapper;
    }


    
  
}
