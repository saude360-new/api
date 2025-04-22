package com.smarthealth.io.smarthealth;

import com.smarthealth.io.smarthealth.dtos.UserCreateDto;
import com.smarthealth.io.smarthealth.dtos.UserResponseDto;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserControllerTest {

    @LocalServerPort
    private int port;

    private final RestTemplate restTemplate = new RestTemplate();

    private String getBaseUrl() {
        return "http://localhost:" + port + "/users";
    }

    @Test
    void testUserCrudFlow() {
        // 1. Criar DTO
        UserCreateDto dto = new UserCreateDto();
        dto.setFirstName("Maria");
        dto.setLastName("Silva");
        dto.setEmailAddress("maria@example.com");
        
        dto.setUserRole("USER");
        
        dto.setGender("F");
        dto.setBirthDate(LocalDateTime.of(1995, 5, 20, 0, 0));

        // 2. Enviar POST para criar usuário
        ResponseEntity<UserResponseDto> createResponse = restTemplate.postForEntity(
                getBaseUrl(), dto, UserResponseDto.class
        );

        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        UserResponseDto createdUser = createResponse.getBody();
        assertNotNull(createdUser);
        assertNotNull(createdUser.getUserId());

        // 3. Buscar usuário por ID
        ResponseEntity<UserResponseDto> getResponse = restTemplate.getForEntity(
                getBaseUrl() + "/" + createdUser.getUserId(), UserResponseDto.class
        );

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals("Maria", getResponse.getBody().getFirstName());

        // 4. Listar todos
        ResponseEntity<UserResponseDto[]> listResponse = restTemplate.getForEntity(
                getBaseUrl(), UserResponseDto[].class
        );

        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        assertTrue(listResponse.getBody().length > 0);

        // 5. Deletar usuário
        restTemplate.delete(getBaseUrl() + "/" + createdUser.getUserId());

        // 6. Verificar que foi deletado
        ResponseEntity<UserResponseDto> deletedResponse = restTemplate.getForEntity(
                getBaseUrl() + "/" + createdUser.getUserId(), UserResponseDto.class
        );

        assertEquals(HttpStatus.NOT_FOUND, deletedResponse.getStatusCode());
    }
}