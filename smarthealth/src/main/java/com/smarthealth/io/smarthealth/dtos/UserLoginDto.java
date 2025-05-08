package com.smarthealth.io.smarthealth.dtos;

import com.smarthealth.io.smarthealth.models.User;

import jakarta.validation.constraints.*;

public class UserLoginDto {

  @NotBlank
  private String emailAdress;
  
  @NotBlank
  private String password;

  public String getEmailAdress() {
    return emailAdress;
  }

  public void setEmailAdress(String emailAdress) {
    this.emailAdress = emailAdress;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  

}
