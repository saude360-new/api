package com.smarthealth.io.smarthealth.controllers;

import com.smarthealth.io.smarthealth.models.UserMetadata;
import com.smarthealth.io.smarthealth.services.UserMetadataService;
import com.smarthealth.io.smarthealth.services.UserService;
import com.smarthealth.io.smarthealth.dtos.UserMetadataDto;
import com.smarthealth.io.smarthealth.mappers.UserMetadataMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userMetadata")
public class UserMetadataController {

    private final UserMetadataService userMetadataService;
    private final UserMetadataMapper userMetadataMapper;
    private final UserService userService;

    public UserMetadataController(UserMetadataService userMetadataService,
                                   UserMetadataMapper userMetadataMapper,
                                   UserService userService) {
        this.userMetadataService = userMetadataService;
        this.userMetadataMapper = userMetadataMapper;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserMetadataDto> create(@RequestBody UserMetadataDto dto) {
        UserMetadata created = userMetadataService.create(dto);
        UserMetadataDto createdDto = userMetadataMapper.toDto(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<UserMetadataDto>> getAll(@PathVariable String id) {
        List<UserMetadata> userMetadataList = userMetadataService.findById(id);
        List<UserMetadataDto> dtoList = userMetadataList.stream()
                .map(UserMetadataMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtoList);
    }
}
