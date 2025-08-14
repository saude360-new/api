package com.smarthealth.io.smarthealth.services;

import com.smarthealth.io.smarthealth.exceptions.ResourceNotFoundException;
import com.smarthealth.io.smarthealth.models.AccountsRelationship;
import com.smarthealth.io.smarthealth.models.User;
import com.smarthealth.io.smarthealth.repositories.AccountsRelationshipRepository;
import com.smarthealth.io.smarthealth.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Serviço para gerenciar relacionamentos entre contas de usuários.
 */
@Service
public class AccountsRelationshipService {

    private static final Logger logger = LoggerFactory.getLogger(AccountsRelationshipService.class);

    private final AccountsRelationshipRepository accountsRelationshipRepository;
    private final UserRepository userRepository; // Para buscar os objetos User

    public AccountsRelationshipService(AccountsRelationshipRepository accountsRelationshipRepository, UserRepository userRepository) {
        this.accountsRelationshipRepository = accountsRelationshipRepository;
        this.userRepository = userRepository;
    }

    /**
     * Cria um novo relacionamento entre contas.
     * @param patientId ID do paciente.
     * @param caregiverId ID do cuidador.
     * @return O relacionamento criado.
     * @throws ResourceNotFoundException se o paciente ou cuidador não forem encontrados.
     */
    @Transactional
    public AccountsRelationship createRelationship(String patientEmail, String caregiverEmail) {
        logger.info("Tentando criar relacionamento entre paciente: {} e cuidador: {}", patientEmail, caregiverEmail);

        User patient = userRepository.findByEmailAddress(patientEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário (Paciente)", patientEmail));
        User caregiver = userRepository.findByEmailAddress(caregiverEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário (Cuidador)", caregiverEmail));

        if (accountsRelationshipRepository.existsByCaregiverAndPatient(caregiver, patient)) {
            logger.warn("Relacionamento entre paciente {} e cuidador {} já existe.", patientEmail, caregiverEmail);
            // Poderíamos lançar uma exceção específica aqui, mas por enquanto, apenas retornamos o existente ou tratamos.
            return accountsRelationshipRepository.findByCaregiverAndPatient(caregiver, patient).get();
        }

        AccountsRelationship newRelationship = new AccountsRelationship(patient, caregiver);
        AccountsRelationship savedRelationship = accountsRelationshipRepository.save(newRelationship);
        logger.info("Relacionamento criado com sucesso com ID: {}", savedRelationship.getAccounts_relationship_row_id());
        return savedRelationship;
    }

    /**
     * Busca um relacionamento por ID.
     * @param id ID do relacionamento.
     * @return Optional contendo o relacionamento se encontrado.
     */
    public Optional<AccountsRelationship> findById(Integer id) {
        logger.debug("Buscando relacionamento com ID: {}", id);
        return accountsRelationshipRepository.findById(id);
    }

    /**
     * Busca todos os relacionamentos onde o usuário é o cuidador.
     * @param caregiverId ID do cuidador.
     * @return Lista de relacionamentos.
     * @throws ResourceNotFoundException se o cuidador não for encontrado.
     */
    public List<AccountsRelationship> findByCaregiverId(String caregiverId) {
        logger.debug("Buscando relacionamentos para cuidador com ID: {}", caregiverId);
        User caregiver = userRepository.findById(caregiverId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário (Cuidador)", caregiverId));
        return accountsRelationshipRepository.findByCaregiver(caregiver);
    }

    /**
     * Busca todos os relacionamentos onde o usuário é o paciente.
     * @param patientId ID do paciente.
     * @return Lista de relacionamentos.
     * @throws ResourceNotFoundException se o paciente não for encontrado.
     */
    public List<AccountsRelationship> findByPatientId(String patientId) {
        logger.debug("Buscando relacionamentos para paciente com ID: {}", patientId);
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário (Paciente)", patientId));
        return accountsRelationshipRepository.findByPatient(patient);
    }


    /**
     * Deleta um relacionamento por ID.
     * @param id ID do relacionamento.
     * @throws ResourceNotFoundException se o relacionamento não for encontrado.
     */
    @Transactional
    public void deleteById(Integer id) {
        logger.warn("Tentativa de exclusão de relacionamento com ID: {}", id);
        if (!accountsRelationshipRepository.existsById(id)) {
            logger.error("Relacionamento não encontrado para exclusão com ID: {}", id);
            throw new ResourceNotFoundException("Relacionamento de Conta", id.toString());
        }
        accountsRelationshipRepository.deleteById(id);
        logger.info("Relacionamento com ID {} excluído com sucesso.", id);
    }

    /**
     * Deleta um relacionamento específico entre um cuidador e um paciente.
     * @param caregiverId ID do cuidador.
     * @param patientId ID do paciente.
     * @throws ResourceNotFoundException se o relacionamento não for encontrado.
     */
    @Transactional
    public void deleteRelationship(String caregiverId, String patientId) {
        logger.warn("Tentativa de exclusão de relacionamento entre cuidador: {} e paciente: {}", caregiverId, patientId);
        User caregiver = userRepository.findById(caregiverId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário (Cuidador)", caregiverId));
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário (Paciente)", patientId));

        if (!accountsRelationshipRepository.existsByCaregiverAndPatient(caregiver, patient)) {
            logger.error("Relacionamento não encontrado para exclusão entre cuidador: {} e paciente: {}", caregiverId, patientId);
            throw new ResourceNotFoundException("Relacionamento de Conta", String.format("cuidador %s e paciente %s", caregiverId, patientId));
        }
        accountsRelationshipRepository.deleteByCaregiverAndPatient(caregiver, patient);
        logger.info("Relacionamento entre cuidador: {} e paciente: {} excluído com sucesso.", caregiverId, patientId);
    }
}