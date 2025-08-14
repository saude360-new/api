package com.smarthealth.io.smarthealth.repositories;

import com.smarthealth.io.smarthealth.models.AccountsRelationship;
import com.smarthealth.io.smarthealth.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para gerenciar relacionamentos entre contas.
 */
@Repository
public interface AccountsRelationshipRepository extends JpaRepository<AccountsRelationship, Integer> {

    /**
     * Busca relacionamentos por cuidador.
     * @param caregiver O objeto User que representa o cuidador.
     * @return Lista de relacionamentos.
     */
    List<AccountsRelationship> findByCaregiver(User caregiver);

    /**
     * Busca relacionamentos por paciente.
     * @param patient O objeto User que representa o paciente.
     * @return Lista de relacionamentos.
     */
    List<AccountsRelationship> findByPatient(User patient);

    /**
     * Busca um relacionamento específico entre um cuidador e um paciente.
     * @param caregiver O objeto User que representa o cuidador.
     * @param patient O objeto User que representa o paciente.
     * @return Optional contendo o relacionamento se encontrado.
     */
    Optional<AccountsRelationship> findByCaregiverAndPatient(User caregiver, User patient);

    /**
     * Verifica se um relacionamento específico entre um cuidador e um paciente existe.
     * @param caregiver O objeto User que representa o cuidador.
     * @param patient O objeto User que representa o paciente.
     * @return true se o relacionamento existir, false caso contrário.
     */
    boolean existsByCaregiverAndPatient(User caregiver, User patient);

    /**
     * Deleta um relacionamento específico entre um cuidador e um paciente.
     * @param caregiver O objeto User que representa o cuidador.
     * @param patient O objeto User que representa o paciente.
     */
    void deleteByCaregiverAndPatient(User caregiver, User patient);
}