package com.smarthealth.io.smarthealth.repositories;

import com.smarthealth.io.smarthealth.models.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;



public interface UserRepository extends JpaRepository<User, String> {

  Optional<User> findByEmailAddress(String emailAddress);
  
}

