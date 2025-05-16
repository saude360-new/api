package com.smarthealth.io.smarthealth.controllers;

import com.smarthealth.io.smarthealth.models.User;
import com.smarthealth.io.smarthealth.services.UserService;
import com.smarthealth.io.smarthealth.dtos.UserCreateDto;
import com.smarthealth.io.smarthealth.dtos.UserLoginDto;
import com.smarthealth.io.smarthealth.dtos.UserResponseDto;
import com.smarthealth.io.smarthealth.mappers.UserMapper;


import com.smarthealth.io.smarthealth.models.UserMetadata;
import com.smarthealth.io.smarthealth.services.AccountsRelationshipService;
import com.smarthealth.io.smarthealth.services.UserMetadataService;
import com.smarthealth.io.smarthealth.dtos.UserMetadataDto;
import com.smarthealth.io.smarthealth.mappers.UserMetadataMapper;

import com.smarthealth.io.smarthealth.models.AccountsRelationship;

import com.smarthealth.io.smarthealth.models.UserMetadata;
import com.smarthealth.io.smarthealth.services.UserMetadataService;
import com.smarthealth.io.smarthealth.dtos.UserMetadataDto;
import com.smarthealth.io.smarthealth.dtos.UserResponseDto;
import com.smarthealth.io.smarthealth.mappers.UserMapper;
import com.smarthealth.io.smarthealth.mappers.UserMetadataMapper;

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
    private final UserMetadataService userMetadataService;
    private final AccountsRelationshipService accountsRelationshipService;

 
 
    public UserController(UserService userService, UserMapper userMapper,UserMetadataService userMetadataService,UserMetadataMapper userMetadataMapper, AccountsRelationshipService accountsRelationshipService ) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.userMetadataService = userMetadataService;
        this.accountsRelationshipService = accountsRelationshipService;

    }

  











    @PostMapping
    public ResponseEntity<UserResponseDto> create(@RequestBody UserCreateDto dto) {

            try{
      
            User saved = userService.create(userMapper.fromDto(dto));

            UserResponseDto response = userMapper.toResponse(saved);

            
              

            if (dto.getPatientEmail() != null && !dto.getPatientEmail().equals("null")){

              accountsRelationshipService.create(dto.getPatientEmail(), response.getUserId());


            }

            return ResponseEntity.status(HttpStatus.CREATED).body(response);}

            catch (Exception e) {
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

      try{
      User user = userService.findById(id)
      .orElseThrow(() -> new RuntimeException("Usuario não encontrado: "+ id));

      UserResponseDto response = userMapper.toResponse(user);
      response.setMetadata(userMetadataService.findById(response.getUserId()));

      switch (user.getUserRole()) {
        case caregiver:
            response.setAr(accountsRelationshipService.findByCaregiverId(id));
          
          break;
      
        case patient:
           response.setAr(accountsRelationshipService.findByPatientId(id));

          break;
      }
      

      return ResponseEntity.status(HttpStatus.CREATED).body(response);}
      catch(Exception e) {
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

      
    }












    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    












    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody UserLoginDto loginDto) {
    
      System.out.println(loginDto.getEmailAdress()+ loginDto.getPassword());
      
      Optional<User> optionalUser = userService.authenticate(loginDto);

    if (optionalUser.isEmpty()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    User user = optionalUser.get();
    UserResponseDto dto = userMapper.toResponse(user);

    return ResponseEntity.ok(dto);
}












    
    public ResponseEntity<List<UserMetadataDto>> getAllMetada(String id) {
    List<UserMetadata> userMetadata = userMetadataService.findById(id);
    List<UserMetadataDto> dtoList = userMetadata.stream()
    .map(UserMetadataMapper::toDto) 
            .toList();
    return ResponseEntity.ok(dtoList);
    }
}
