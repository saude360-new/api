package com.smarthealth.io.smarthealth.repositories;

import com.smarthealth.io.smarthealth.models.UserMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserMetadataRepository extends JpaRepository<UserMetadata, String> {

  List<UserMetadata> findByUserUserId(String userID);
}
