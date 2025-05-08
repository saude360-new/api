package com.smarthealth.io.smarthealth.dtos;


import com.smarthealth.io.smarthealth.models.User;
import com.smarthealth.io.smarthealth.models.User.UserRole;

import jakarta.validation.constraints.*;


import java.time.*;

public class UserCreateDto {


  @NotBlank
  private String password;

  @NotBlank
  private String firstName;

  @NotBlank
  private String lastName;

  @NotBlank
  private  LocalDate birthDate;

  @NotBlank
  private String gender;

  @NotBlank
  private String emailAddress;

  @NotNull
  private User.UserRole userRole;

  @NotBlank
  private String emailHash;

  @NotBlank
  private String passworDigest;
  
  @NotBlank
  private String salt;

  @NotBlank
  private String symmetricKey;

  @NotBlank
  private String publicKey;

  @NotBlank
  private String privateKey;

  private String patientID;

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
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

  public User.UserRole getUserRole() {
    return userRole;
  }

  public void setUserRole(User.UserRole userRole) {
    this.userRole = userRole;
  }

  public String getEmailHash() {
    return emailHash;
  }

  public void setEmailHash(String emailHash) {
    this.emailHash = emailHash;
  }

  public String getPassworDigest() {
    return passworDigest;
  }

  public void setPassworDigest(String passworDigest) {
    this.passworDigest = passworDigest;
  }

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
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

  public String getPatientID() {
    return patientID;
  }

  public void setPatientID(String patientID) {
    this.patientID = patientID;
  }

  

  


  

 
  
}
