/*package com.smarthealth.io.smarthealth;

import com.smarthealth.io.smarthealth.dtos.UserCreateDto;
import com.smarthealth.io.smarthealth.dtos.UserResponseDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerTest {

    @LocalServerPort
    private int port;

    private static RestTemplate rest;

    @BeforeAll
    public static void setupRestTemplate() {
        rest = new RestTemplate();
        rest.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                String body = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                System.err.println("\u001B[31m>> RESPONSE ERROR BODY:\n" + body + "\u001B[0m");
                super.handleError(response);
            }
        });
    }

    private String baseUrl() {
        return "http://localhost:" + port + "/users";
    }

    @Test
    void testUserCrudFlow() {
        
        System.out.println("\u001B[36m[TEST] Criando usuário...\u001B[0m");
        UserCreateDto dto = new UserCreateDto();
        dto.setFirstName("Maria");
        dto.setLastName("Silva");
        dto.setEmailAddress("maria@example.com");
        dto.setUserRole("USER");
        dto.setGender("F");
        dto.setBirthDate(LocalDateTime.of(1995, 5, 20, 0, 0));

        ResponseEntity<UserResponseDto> createResponse = rest.postForEntity(baseUrl(), dto, UserResponseDto.class);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        UserResponseDto created = createResponse.getBody();
        assertNotNull(created);
        System.out.println("\u001B[32m✔ Usuário criado com ID: " + created.getUserId() + "\u001B[0m");

        
        System.out.println("\u001B[36m[TEST] Buscando usuário por ID...\u001B[0m");
        ResponseEntity<UserResponseDto> getResponse = rest.getForEntity(baseUrl() + "/" + created.getUserId(), UserResponseDto.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        System.out.println("\u001B[32m✔ GET /users/{id} retornou com sucesso.\u001B[0m");

        
        System.out.println("\u001B[36m[TEST] Listando usuários...\u001B[0m");
        ResponseEntity<UserResponseDto[]> listResponse = rest.getForEntity(baseUrl(), UserResponseDto[].class);
        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        assertTrue(listResponse.getBody().length >= 1);
        System.out.println("\u001B[32m✔ GET /users listou pelo menos um usuário.\u001B[0m");

        
        System.out.println("\u001B[36m[TEST] Deletando usuário...\u001B[0m");
        rest.delete(baseUrl() + "/" + created.getUserId());
        System.out.println("\u001B[32m✔ DELETE /users/{id} executado com sucesso.\u001B[0m");

        
        System.out.println("\u001B[36m[TEST] Verificando 404 após delete...\u001B[0m");
        HttpClientErrorException.NotFound notFoundException = assertThrows(
            HttpClientErrorException.NotFound.class,
            () -> rest.getForEntity(baseUrl() + "/" + created.getUserId(), UserResponseDto.class)
        );
        assertEquals(HttpStatus.NOT_FOUND, notFoundException.getStatusCode());
        System.out.println("\u001B[32m✔ GET após delete retornou 404 como esperado.\u001B[0m");
    }
}*/
