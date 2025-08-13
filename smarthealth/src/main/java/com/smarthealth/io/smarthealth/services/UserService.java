package com.smarthealth.io.smarthealth.services;

import com.smarthealth.io.smarthealth.dtos.UserLoginDto;
import com.smarthealth.io.smarthealth.exceptions.InvalidCredentialsException;
import com.smarthealth.io.smarthealth.exceptions.ResourceNotFoundException;
import com.smarthealth.io.smarthealth.exceptions.UserAlreadyExistsException;
import com.smarthealth.io.smarthealth.models.User;
import com.smarthealth.io.smarthealth.repositories.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Serviço para gerenciamento de usuários.
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Cria um novo usuário, verificando se o email já existe.
     */
    public User create(User user) {
        logger.info("Criando novo usuário com email: {}", user.getEmailAddress());
        
        // Verificar se o usuário já existe
        if (userRepository.findByEmailAddress(user.getEmailAddress()).isPresent()) {
            throw new UserAlreadyExistsException(user.getEmailAddress());
        }
        
        User savedUser = userRepository.save(user);
        logger.info("Usuário criado com sucesso. ID: {}", savedUser.getUserId());
        
        return savedUser;
    }

    /**
     * Busca todos os usuários.
     */
    public List<User> findAll() {
        logger.debug("Buscando todos os usuários");
        return userRepository.findAll();
    }

    /**
     * Busca um usuário por ID.
     */
    public Optional<User> findById(String id) {
        logger.debug("Buscando usuário por ID: {}", id);
        return userRepository.findById(id);
    }

    /**
     * Busca um usuário por email.
     */
    public Optional<User> findByEmail(String email) {
        logger.debug("Buscando usuário por email: {}", email);
        return userRepository.findByEmailAddress(email);
    }

    /**
     * Remove um usuário por ID.
     */
    public void deleteById(String id) {
        logger.info("Removendo usuário com ID: {}", id);
        
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário", id);
        }
        
        userRepository.deleteById(id);
        logger.info("Usuário removido com sucesso. ID: {}", id);
    }

    /**
     * Autentica um usuário usando email e senha.
     * Retorna um token JWT em caso de sucesso.
     */
    public String authenticate(UserLoginDto dto) {
        logger.info("Tentativa de autenticação para email: {}", dto.getEmailAddress());
        
        User user = findByEmail(dto.getEmailAddress())
            .orElseThrow(() -> {
                logger.warn("Tentativa de login com email inexistente: {}", dto.getEmailAddress());
                return new InvalidCredentialsException();
            });

        // Verificar senha usando BCrypt
        if (!passwordEncoder.matches(dto.getPassword(), user.getPasswordDigest())) {
            logger.warn("Tentativa de login com senha incorreta para email: {}", dto.getEmailAddress());
            throw new InvalidCredentialsException();
        }

        logger.info("Autenticação bem-sucedida para usuário: {}", user.getUserId());
        
        // TODO: Implementar geração de JWT token
        // Por enquanto, retorna um token simulado
        return generateJwtToken(user);
    }

    /**
     * Gera um token JWT para o usuário.
     * TODO: Implementar geração real de JWT.
     */
    private String generateJwtToken(User user) {
        // Implementação simplificada - em produção, usar biblioteca JWT
        return "jwt_token_for_user_" + user.getUserId();
    }
}

