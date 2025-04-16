package com.smarthealth.io.smarthealth.controllers;

import com.smarthealth.io.smarthealth.models.User;
import com.smarthealth.io.smarthealth.services.UserService;
import com.smarthealth.io.smarthealth.dtos.UserCreateDto;
import com.smarthealth.io.smarthealth.dtos.UserResponseDto;
import com.smarthealth.io.smarthealth.mappers.UserMapper;


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
 
    public UserController(UserService userService , UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

  


    @PostMapping 
    public ResponseEntity<UserResponseDto> create(@RequestBody UserCreateDto user) { 
        User created = userService.create(userMapper.fromDto(user));
       return ResponseEntity.ok(userMapper.toResponse(created));
       }


       @GetMapping 
      public ResponseEntity<List<UserResponseDto>> getAll() {
    List<User> users = userService.findAll();
    List<UserResponseDto> dtoList = users.stream()
    .map(UserMapper::toResponse) 
            .toList();
    return ResponseEntity.ok(dtoList);
}
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable String id) {

      Optional<User> optionalUser = userService.findById(id);
      return optionalUser
              .map(UserMapper::toResponse)
              .map(ResponseEntity::ok)
              .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
