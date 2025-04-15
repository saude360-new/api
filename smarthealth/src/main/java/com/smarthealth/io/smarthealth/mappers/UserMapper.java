package com.smarthealth.io.smarthealth.mappers;

import com.smarthealth.io.smarthealth.dtos.UserCreateDto;
import com.smarthealth.io.smarthealth.dtos.UserResponseDto; 
import com.smarthealth.io.smarthealth.models.User;

import java.time.LocalDateTime;


public class UserMapper {
  
  public static User fromDto(UserCreateDto dto) {
    User user = new User();
    user.setFirstName(dto.getFirstName());
    user.setLastName(dto.getLastName());
    user.setEmailAddress(dto.getEmailAddress());
    user.setGender(dto.getGender());
    user.setBirthDate(dto.getBirthDate());

    
    user.setPasswordDigest("hashed_" + dto.getPassword()); // dado mockado so resolver dps de td pronto
    user.setSalt("saltzinho");
    user.setCreatedAt(LocalDateTime.now());

    return user;
}


// Entrada: User → Saída: UserResponseDTO
public static UserResponseDto toResponse(User user) {
  UserResponseDto dto = new UserResponseDto();
  dto.setUserId(user.getUserId());
  dto.setFirstName(user.getFirstName());
  dto.setLastName(user.getLastName());
  dto.setEmailAddress(user.getEmailAddress());
  dto.setCreatedAt(user.getCreatedAt());
  return dto;
}


}
