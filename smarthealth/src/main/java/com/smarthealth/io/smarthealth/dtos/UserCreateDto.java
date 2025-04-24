package com.smarthealth.io.smarthealth.dtos;

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

  @NotBlank
  private String userRole;

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

  public String getUserRole() {
    return userRole;
  }

  public void setUserRole(String userRole) {
    this.userRole = userRole;
  }

  

  

 
  
}
