package com.smarthealth.io.smarthealth.dtos;

import com.smarthealth.io.smarthealth.models.User;

import jakarta.validation.constraints.*;

public class UserLoginDto {

  @NotBlank
  private String emailAddress;
  
  @NotBlank
  private String password;

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAdress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  

}
