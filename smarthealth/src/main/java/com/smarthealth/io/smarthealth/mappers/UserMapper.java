package com.smarthealth.io.smarthealth.mappers;

import com.smarthealth.io.smarthealth.dtos.UserCreateDto;
import com.smarthealth.io.smarthealth.dtos.UserResponseDto; 
import com.smarthealth.io.smarthealth.models.User;
import com.smarthealth.io.smarthealth.models.User.UserRole;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {
  
  public User fromDto(UserCreateDto dto) {
    User user = new User();
    user.setFirstName(dto.getFirstName());
    user.setLastName(dto.getLastName());
    user.setEmailAddress(dto.getEmailAddress());
    user.setGender(dto.getGender());
    user.setBirthDate(dto.getBirthDate());

    // Conversão segura de String para Enum
    user.setUserRole(dto.getUserRole());

    user.setEmailHash("hashed_" + dto.getEmailAddress());
    user.setPasswordDigest("hashed_" + dto.getPassword());
    user.setSalt("saltzinho");
    user.setPrivateKey("Private key");
    user.setPublicKey("Public key");
    user.setSymmetricKey("Symetric key");
    user.setCreatedAt(LocalDateTime.now());
    user.setUpdatedAt(LocalDateTime.now());

    return user;
}


// Entrada: User → Saída: UserResponseDTO
public  UserResponseDto toResponse(User user) {
  UserResponseDto dto = new UserResponseDto();
  dto.setUserId(user.getUserId());
  dto.setFirstName(user.getFirstName());
  dto.setLastName(user.getLastName());
  dto.setEmailAddress(user.getEmailAddress());
  dto.setCreatedAt(user.getCreatedAt());
  return dto;
}


}
