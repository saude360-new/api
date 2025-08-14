package com.smarthealth.io.smarthealth.controllers;

import com.smarthealth.io.smarthealth.dtos.UserCreateDto;
import com.smarthealth.io.smarthealth.dtos.UserLoginDto;
import com.smarthealth.io.smarthealth.dtos.UserResponseDto;
import com.smarthealth.io.smarthealth.exceptions.InvalidCredentialsException;
import com.smarthealth.io.smarthealth.exceptions.ResourceNotFoundException;
import com.smarthealth.io.smarthealth.mappers.UserMapper;
import com.smarthealth.io.smarthealth.models.User;
import com.smarthealth.io.smarthealth.services.UserService;
import com.smarthealth.io.smarthealth.services.AccountsRelationshipService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Controller REST para gerenciamento de usuários.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final UserMapper userMapper;
    private final AccountsRelationshipService accountsRelationshipService;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper, AccountsRelationshipService accountsRelationshipService  ) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.accountsRelationshipService = accountsRelationshipService;
    }

    /**
     * Cria um novo usuário.
     */
    @PostMapping
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserCreateDto dto) {
        logger.info("Recebida requisição para criar usuário com email: {}", dto.getEmailAddress());
        
        User user = userMapper.fromDto(dto);
        User savedUser = userService.create(user);
        UserResponseDto response = userMapper.toResponse(savedUser, savedUser.getUserRole().toString());
         logger.info("Usuário criado com sucesso. ID: {}", savedUser.getUserId());


        if (dto.getPatientEmail() != null && !dto.getPatientEmail().trim().isEmpty()) {
    

    accountsRelationshipService.createRelationship(
            dto.getPatientEmail(),
            dto.getEmailAddress()
    );
    
}


       
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Busca todos os usuários.
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAll() {
        logger.debug("Recebida requisição para buscar todos os usuários");
        
        List<User> users = userService.findAll();
      List<UserResponseDto> dtoList = users.stream()
          .map(user -> userMapper.toResponse(user, user.getUserRole().toString()))
          .toList();
        
        return ResponseEntity.ok(dtoList);
    }

    /**
     * Busca um usuário por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable String id) {
        logger.debug("Recebida requisição para buscar usuário com ID: {}", id);
        
        User user = userService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", id));

        UserResponseDto response = userMapper.toResponse(user, user.getUserRole().toString() );
        return ResponseEntity.ok(response);
    }

    /**
     * Remove um usuário por ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        logger.info("Recebida requisição para remover usuário com ID: {}", id);
        
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Autentica um usuário.
     */
    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@Valid @RequestBody UserLoginDto loginDto) {
        logger.info("Recebida requisição de login para email: {}", loginDto.getEmailAddress());
        
        String token = userService.authenticate(loginDto);
        
        User user = userService.findByEmail(loginDto.getEmailAddress())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", loginDto.getEmailAddress()));

        UserResponseDto response = userMapper.toResponse(user, user.getUserRole().toString());
        
        // TODO: Incluir token na resposta quando implementar JWT completamente
        logger.info("Login realizado com sucesso para usuário: {}", user.getUserId());
        
        return ResponseEntity.ok(response);
    }
}

