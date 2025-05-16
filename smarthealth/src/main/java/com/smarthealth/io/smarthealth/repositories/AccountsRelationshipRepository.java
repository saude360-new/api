package com.smarthealth.io.smarthealth.repositories;

import com.smarthealth.io.smarthealth.models.AccountsRelationship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountsRelationshipRepository extends  JpaRepository<AccountsRelationship, String>{
  
  List<AccountsRelationship> findByPatientUserId(String patient);
  List<AccountsRelationship> findByCaregiverUserId(String caregiver);

}
