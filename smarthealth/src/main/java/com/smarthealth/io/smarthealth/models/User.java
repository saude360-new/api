package com.smarthealth.io.smarthealth.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

//import org.springframework.boot.autoconfigure.security.SecurityProperties.User;


  @Entity
  @Table(name = "users")
  public class User{

    public enum UserRole {
      caregiver,
      patient
    }

      @Id
      @GeneratedValue( strategy = GenerationType.UUID)
      @Column(name = "user_id")
      private String userId;

      @Column(name = "first_name", nullable = false)
      private String firstName;

      @Column(name = "last_name", nullable = false)
      private String lastName;

      @Column(name = "birth_date", nullable = false)
      private LocalDate birthDate;

      @Column(name = "gender", nullable = false)
      private String gender;

      @Column(name = "email_address", nullable = false)
      private String emailAddress;

      @Column(name = "email_hash", nullable = false)
      private String emailHash;

      @Column(name = "password_digest", nullable = false)
      private String passwordDigest;

      @Column(name = "salt", nullable = false, unique = true)
      private String salt;

      @Enumerated(EnumType.STRING)
      @Column(name = "role", nullable = false)
      private UserRole userRole;

      @Column(name = "symmetric_key", nullable = false)
      private String symmetricKey;

      @Column(name = "public_key", nullable = false)
      private String publicKey;

      @Column(name = "private_key", nullable = false)
      private String privateKey;

      @Column(name = "created_at", nullable = false, updatable = false)
      private LocalDateTime createdAt;

          //eu acho que isso precisa poder ser nulo, se não ao criar vc tem que colocar algo
      @Column(name = "updated_at", nullable = false)
      private LocalDateTime updatedAt;

  

      public User() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
      }



      public String getUserId() {
        return userId;
      }



      public void setUserId(String userId) {
        this.userId = userId;
      }



      public String getFirstName() {
        return firstName;
      }



      public void setFirstName(String firstName) {
        this.firstName = firstName;
      }



      public String getLastName() {
        return lastName;
      }



      public void setLastName(String lastName) {
        this.lastName = lastName;
      }



      public LocalDate getBirthDate() {
        return birthDate;
      }



      public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
      }



      public String getGender() {
        return gender;
      }



      public void setGender(String gender) {
        this.gender = gender;
      }



      public String getEmailAddress() {
        return emailAddress;
      }



      public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
      }



      public String getEmailHash() {
        return emailHash;
      }



      public void setEmailHash(String emailHash) {
        this.emailHash = emailHash;
      }



      public String getPasswordDigest() {
        return passwordDigest;
      }



      public void setPasswordDigest(String passwordDigest) {
        this.passwordDigest = passwordDigest;
      }



      public String getSalt() {
        return salt;
      }



      public void setSalt(String salt) {
        this.salt = salt;
      }



      public UserRole getUserRole() {
        return userRole;
      }



      public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
      }



      public String getSymmetricKey() {
        return symmetricKey;
      }



      public void setSymmetricKey(String symmetricKey) {
        this.symmetricKey = symmetricKey;
      }



      public String getPublicKey() {
        return publicKey;
      }



      public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
      }



      public String getPrivateKey() {
        return privateKey;
      }



      public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
      }



      public LocalDateTime getCreatedAt() {
        return createdAt;
      }



      public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
      }



      public LocalDateTime getUpdatedAt() {
        return updatedAt;
      }



      public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
      }



      
  
}

