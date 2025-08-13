package com.smarthealth.io.smarthealth.mappers;

import com.smarthealth.io.smarthealth.dtos.UserCreateDto;
import com.smarthealth.io.smarthealth.dtos.UserResponseDto;
import com.smarthealth.io.smarthealth.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

/**
 * Mapper para conversão entre entidades User e DTOs.
 */
@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;
    private final SecureRandom secureRandom;

    @Autowired
    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.secureRandom = new SecureRandom();
    }

    /**
     * Converte um DTO de criação para entidade User.
     * Aplica hashing seguro de senha e gera chaves criptográficas.
     */
    public User fromDto(UserCreateDto dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmailAddress(dto.getEmailAddress());
        user.setGender(dto.getGender());
        user.setBirthDate(dto.getBirthDate());
        user.setUserRole(dto.getUserRole());

        // Gerar salt seguro
        byte[] saltBytes = new byte[32];
        secureRandom.nextBytes(saltBytes);
        String salt = Base64.getEncoder().encodeToString(saltBytes);

        // Hash seguro da senha usando BCrypt
        String hashedPassword = passwordEncoder.encode(dto.getPassword());

        // Hash do email para indexação
        String emailHash = passwordEncoder.encode(dto.getEmailAddress());

        // Gerar chaves criptográficas seguras (simulação - em produção usar bibliotecas apropriadas)
        String privateKey = generateSecureKey(64);
        String publicKey = generateSecureKey(64);
        String symmetricKey = generateSecureKey(32);

        user.setEmailHash(emailHash);
        user.setPasswordDigest(hashedPassword);
        user.setSalt(salt);
        user.setPrivateKey(privateKey);
        user.setPublicKey(publicKey);
        user.setSymmetricKey(symmetricKey);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return user;
    }

    /**
     * Converte uma entidade User para DTO de resposta.
     * Remove informações sensíveis da resposta.
     */
    public UserResponseDto toResponse(User user) {
        if (user == null) {
            return null;
        }

        UserResponseDto dto = new UserResponseDto();
        dto.setUserId(user.getUserId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmailAddress(user.getEmailAddress());
        dto.setCreatedAt(user.getCreatedAt());
        
        return dto;
    }

    /**
     * Gera uma chave segura usando SecureRandom.
     */
    private String generateSecureKey(int length) {
        byte[] keyBytes = new byte[length];
        secureRandom.nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes);
    }
}

