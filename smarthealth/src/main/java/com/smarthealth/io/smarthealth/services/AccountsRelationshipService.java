package com.smarthealth.io.smarthealth.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.smarthealth.io.smarthealth.dtos.UserMetadataDto;
import com.smarthealth.io.smarthealth.mappers.UserMetadataMapper;
import com.smarthealth.io.smarthealth.models.AccountsRelationship;
import com.smarthealth.io.smarthealth.repositories.AccountsRelationshipRepository;
import com.smarthealth.io.smarthealth.models.User;
import com.smarthealth.io.smarthealth.models.UserMetadata;



@Service
public class AccountsRelationshipService {
  private final AccountsRelationshipRepository accountsRelationshipRepository;
  private final UserService userService;

  public AccountsRelationshipService(AccountsRelationshipRepository accountsRelationshipRepository, UserService userService) {
    this.accountsRelationshipRepository = accountsRelationshipRepository;
    this.userService = userService;
  }


  //como o userservice devolve um optiona<user> e o mapper espera um user precisamos fazer essa gambiarra de lançar erro
  public AccountsRelationship create(String patientEmail, String caregiverId) {

    User patient = userService.findByEmail(patientEmail)
    .orElseThrow(() -> new RuntimeException("Paciente não encontrado: "+patientEmail));

    User caregiver = userService.findById(caregiverId)
    .orElseThrow(() -> new RuntimeException("Usuário não encontrado: "+caregiverId));

    AccountsRelationship ar = new AccountsRelationship(patient, caregiver);
    return accountsRelationshipRepository.save(ar);

  }

    public List<AccountsRelationship> findAll(){
      return accountsRelationshipRepository.findAll();
    }

    public List<AccountsRelationship> findByCaregiverId(String id){
      return accountsRelationshipRepository.findByCaregiverUserId(id);
    }

    public List<AccountsRelationship> findByPatientId(String userId){
      return accountsRelationshipRepository.findByPatientUserId(userId);
    }

    public void deleteById(String id) {
      accountsRelationshipRepository.deleteById(id);
  }
}
