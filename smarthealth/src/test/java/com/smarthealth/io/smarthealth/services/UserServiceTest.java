package com.smarthealth.io.smarthealth.services;

import com.smarthealth.io.smarthealth.dtos.UserLoginDto;
import com.smarthealth.io.smarthealth.exceptions.InvalidCredentialsException;
import com.smarthealth.io.smarthealth.exceptions.ResourceNotFoundException;
import com.smarthealth.io.smarthealth.exceptions.UserAlreadyExistsException;
import com.smarthealth.io.smarthealth.models.User;
import com.smarthealth.io.smarthealth.repositories.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para UserService.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserLoginDto loginDto;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId("test-user-id");
        testUser.setFirstName("João");
        testUser.setLastName("Silva");
        testUser.setEmailAddress("joao@example.com");
        testUser.setPasswordDigest("hashed_password");
        testUser.setGender("M");
        testUser.setBirthDate(LocalDate.of(1990, 1, 1));
        testUser.setUserRole(User.UserRole.patient);
        testUser.setCreatedAt(LocalDateTime.now());

        loginDto = new UserLoginDto();
        loginDto.setEmailAdress("joao@example.com");
        loginDto.setPassword("password123");
    }

    @Test
    void create_ShouldCreateUser_WhenEmailDoesNotExist() {
        // Arrange
        when(userRepository.findByEmailAddress(testUser.getEmailAddress())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        User result = userService.create(testUser);

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getEmailAddress(), result.getEmailAddress());
        verify(userRepository).findByEmailAddress(testUser.getEmailAddress());
        verify(userRepository).save(testUser);
    }

    @Test
    void create_ShouldThrowUserAlreadyExistsException_WhenEmailExists() {
        // Arrange
        when(userRepository.findByEmailAddress(testUser.getEmailAddress())).thenReturn(Optional.of(testUser));

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> userService.create(testUser));
        verify(userRepository).findByEmailAddress(testUser.getEmailAddress());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void findById_ShouldReturnUser_WhenUserExists() {
        // Arrange
        when(userRepository.findById("test-user-id")).thenReturn(Optional.of(testUser));

        // Act
        Optional<User> result = userService.findById("test-user-id");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testUser.getUserId(), result.get().getUserId());
        verify(userRepository).findById("test-user-id");
    }

    @Test
    void deleteById_ShouldDeleteUser_WhenUserExists() {
        // Arrange
        when(userRepository.existsById("test-user-id")).thenReturn(true);

        // Act
        userService.deleteById("test-user-id");

        // Assert
        verify(userRepository).existsById("test-user-id");
        verify(userRepository).deleteById("test-user-id");
    }

    @Test
    void deleteById_ShouldThrowResourceNotFoundException_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.existsById("test-user-id")).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.deleteById("test-user-id"));
        verify(userRepository).existsById("test-user-id");
        verify(userRepository, never()).deleteById(anyString());
    }

    @Test
    void authenticate_ShouldReturnToken_WhenCredentialsAreValid() {
        // Arrange
        when(userRepository.findByEmailAddress(loginDto.getEmailAddress())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(loginDto.getPassword(), testUser.getPasswordDigest())).thenReturn(true);

        // Act
        String result = userService.authenticate(loginDto);

        // Assert
        assertNotNull(result);
        assertTrue(result.startsWith("jwt_token_for_user_"));
        verify(userRepository).findByEmailAddress(loginDto.getEmailAddress());
        verify(passwordEncoder).matches(loginDto.getPassword(), testUser.getPasswordDigest());
    }

    @Test
    void authenticate_ShouldThrowInvalidCredentialsException_WhenUserNotFound() {
        // Arrange
        when(userRepository.findByEmailAddress(loginDto.getEmailAddress())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> userService.authenticate(loginDto));
        verify(userRepository).findByEmailAddress(loginDto.getEmailAddress());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void authenticate_ShouldThrowInvalidCredentialsException_WhenPasswordIsIncorrect() {
        // Arrange
        when(userRepository.findByEmailAddress(loginDto.getEmailAddress())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(loginDto.getPassword(), testUser.getPasswordDigest())).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> userService.authenticate(loginDto));
        verify(userRepository).findByEmailAddress(loginDto.getEmailAddress());
        verify(passwordEncoder).matches(loginDto.getPassword(), testUser.getPasswordDigest());
    }
}

