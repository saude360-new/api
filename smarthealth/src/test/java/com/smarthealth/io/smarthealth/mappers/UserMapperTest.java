package com.smarthealth.io.smarthealth.mappers;

import com.smarthealth.io.smarthealth.dtos.UserCreateDto;
import com.smarthealth.io.smarthealth.dtos.UserResponseDto;
import com.smarthealth.io.smarthealth.models.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Testes unitários para UserMapper.
 */
@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserMapper userMapper;

    private UserCreateDto userCreateDto;
    private User user;

    @BeforeEach
    void setUp() {
        userCreateDto = new UserCreateDto();
        userCreateDto.setFirstName("João");
        userCreateDto.setLastName("Silva");
        userCreateDto.setEmailAddress("joao@example.com");
        userCreateDto.setPassword("password123");
        userCreateDto.setGender("M");
        userCreateDto.setBirthDate(LocalDate.of(1990, 1, 1));
        userCreateDto.setUserRole(User.UserRole.patient);

        user = new User();
        user.setUserId("test-user-id");
        user.setFirstName("João");
        user.setLastName("Silva");
        user.setEmailAddress("joao@example.com");
        user.setGender("M");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setUserRole(User.UserRole.patient);
        user.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void fromDto_ShouldMapUserCreateDtoToUser() {
        // Arrange
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");

        // Act
        User result = userMapper.fromDto(userCreateDto);

        // Assert
        assertNotNull(result);
        assertEquals(userCreateDto.getFirstName(), result.getFirstName());
        assertEquals(userCreateDto.getLastName(), result.getLastName());
        assertEquals(userCreateDto.getEmailAddress(), result.getEmailAddress());
        assertEquals(userCreateDto.getGender(), result.getGender());
        assertEquals(userCreateDto.getBirthDate(), result.getBirthDate());
        assertEquals(userCreateDto.getUserRole(), result.getUserRole());
        
        // Verificar se campos de segurança foram preenchidos
        assertNotNull(result.getPasswordDigest());
        assertNotNull(result.getSalt());
        assertNotNull(result.getEmailHash());
        assertNotNull(result.getPrivateKey());
        assertNotNull(result.getPublicKey());
        assertNotNull(result.getSymmetricKey());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    void fromDto_ShouldReturnNull_WhenDtoIsNull() {
        // Act
        User result = userMapper.fromDto(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toResponse_ShouldMapUserToUserResponseDto() {
        // Act
        UserResponseDto result = userMapper.toResponse(user, user.getUserRole().toString());

        // Assert
        assertNotNull(result);
        assertEquals(user.getUserId(), result.getUserId());
        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(user.getLastName(), result.getLastName());
        assertEquals(user.getEmailAddress(), result.getEmailAddress());
        assertEquals(user.getCreatedAt(), result.getCreatedAt());
    }

    @Test
    void toResponse_ShouldReturnNull_WhenUserIsNull() {
        // Act
        UserResponseDto result = userMapper.toResponse(null,null);

        // Assert
        assertNull(result);
    }

    @Test
    void fromDto_ShouldGenerateSecureKeys() {
        // Arrange
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");

        // Act
        User result = userMapper.fromDto(userCreateDto);

        // Assert
        assertNotNull(result.getSalt());
        assertNotNull(result.getPrivateKey());
        assertNotNull(result.getPublicKey());
        assertNotNull(result.getSymmetricKey());
        
        // Verificar que as chaves são diferentes entre si
        assertNotEquals(result.getPrivateKey(), result.getPublicKey());
        assertNotEquals(result.getPrivateKey(), result.getSymmetricKey());
        assertNotEquals(result.getPublicKey(), result.getSymmetricKey());
        
        // Verificar que as chaves têm tamanho adequado (Base64 encoded)
        assertTrue(result.getSalt().length() > 40); // 32 bytes -> ~44 chars em Base64
        assertTrue(result.getPrivateKey().length() > 80); // 64 bytes -> ~88 chars em Base64
        assertTrue(result.getPublicKey().length() > 80); // 64 bytes -> ~88 chars em Base64
        assertTrue(result.getSymmetricKey().length() > 40); // 32 bytes -> ~44 chars em Base64
    }
}

