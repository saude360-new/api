package com.smarthealth.io.smarthealth.controllers;

import com.smarthealth.io.smarthealth.models.User;
import com.smarthealth.io.smarthealth.services.UserService;
import com.smarthealth.io.smarthealth.dtos.UserCreateDto;
import com.smarthealth.io.smarthealth.dtos.UserLoginDto;
import com.smarthealth.io.smarthealth.dtos.UserResponseDto;
import com.smarthealth.io.smarthealth.mappers.UserMapper;

import com.smarthealth.io.smarthealth.services.AccountsRelationshipService;
import com.smarthealth.io.smarthealth.services.UserMetadataService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Diz que a classe é um controller REST que responde com JSON
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserMetadataService userMetadataService;
    private final AccountsRelationshipService accountsRelationshipService;

    public UserController(
            UserService userService,
            UserMapper userMapper,
            UserMetadataService userMetadataService,
            AccountsRelationshipService accountsRelationshipService
    ) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.userMetadataService = userMetadataService;
        this.accountsRelationshipService = accountsRelationshipService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> create(@RequestBody UserCreateDto dto) {
        User saved = userService.create(userMapper.fromDto(dto));
        UserResponseDto response = userMapper.toResponse(saved);

        if (dto.getPatientEmail() != null && !dto.getPatientEmail().equalsIgnoreCase("null")) {
            accountsRelationshipService.create(dto.getPatientEmail(), response.getUserId());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAll() {
        List<User> users = userService.findAll();
        List<UserResponseDto> dtoList = users.stream()
                .map(userMapper::toResponse)
                .toList();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable String id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + id));

        UserResponseDto response = userMapper.toResponse(user);
        response.setMetadata(userMetadataService.findById(response.getUserId()));

        switch (user.getUserRole()) {
            case caregiver -> response.setAr(accountsRelationshipService.findByCaregiverId(id));
            case patient -> response.setAr(accountsRelationshipService.findByPatientId(id));
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody UserLoginDto loginDto) {
        String token = userService.authenticate(loginDto);
        System.out.println("Token gerado: " + token);

        if ("tokenInativo".equals(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userService.findByEmail(loginDto.getEmailAddress())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        UserResponseDto dto = userMapper.toResponse(user);

        // Aqui você pode incluir o token no DTO, se quiser
        // dto.setToken(token);

        return ResponseEntity.ok(dto);
    }
}
