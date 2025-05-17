package com.smarthealth.io.smarthealth.controllers;


import com.smarthealth.io.smarthealth.models.User;
import com.smarthealth.io.smarthealth.models.UserMetadata;
import com.smarthealth.io.smarthealth.services.UserMetadataService;
import com.smarthealth.io.smarthealth.services.UserService;
import com.smarthealth.io.smarthealth.dtos.UserMetadataDto;
import com.smarthealth.io.smarthealth.dtos.UserResponseDto;
import com.smarthealth.io.smarthealth.mappers.UserMapper;
import com.smarthealth.io.smarthealth.mappers.UserMetadataMapper;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController


@RequestMapping("/userMetadata")
public class UserMetadataController {

  private final UserMetadataService userMetadataService;
  private final UserMetadataMapper userMetadataMapper;
  private final UserService userService;
 
 
    public UserMetadataController(UserMetadataService userMetadataService, UserMetadataMapper userMetadataMapper, UserService userService ) {
        this.userMetadataService = userMetadataService;
        this.userMetadataMapper = userMetadataMapper;
        this.userService = userService;

    }

    @PostMapping
    public ResponseEntity<UserMetadataDto> create(@RequestBody UserMetadataDto dto){


      UserMetadata created = userMetadataService.create(dto);

      return ResponseEntity.status(HttpStatus.CREATED).body(dto);

    }



    @GetMapping("/{id}")
    public ResponseEntity<List<UserMetadataDto>> getAll(String id) {
    List<UserMetadata> userMetadata = userMetadataService.findById(id);
    List<UserMetadataDto> dtoList = userMetadata.stream()
    .map(UserMetadataMapper::toDto) 
            .toList();
    return ResponseEntity.ok(dtoList);
    }
  
}
