package com.smarthealth.io.smarthealth.controllers;

import com.smarthealth.io.smarthealth.models.User;
import com.smarthealth.io.smarthealth.services.UserService;
import com.smarthealth.io.smarthealth.dtos.UserCreateDto;
import com.smarthealth.io.smarthealth.dtos.UserResponseDto;
import com.smarthealth.io.smarthealth.mappers.UserMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


//diz que ela vai receber requisiçoes e devolver json
@RestController


@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
 
 
    public UserController(UserService userService, UserMapper userMapper ) {
        this.userService = userService;
        this.userMapper = userMapper;

    }

  


    @PostMapping
    public ResponseEntity<UserResponseDto> create(@RequestBody UserCreateDto dto) {
        try {
    
            User entity = userMapper.fromDto(dto);

            User saved = userService.create(entity);

            UserResponseDto response = userMapper.toResponse(saved);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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

      Optional<User> optionalUser = userService.findById(id);
      return optionalUser
              .map(userMapper::toResponse)
              .map(ResponseEntity::ok)
              .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
