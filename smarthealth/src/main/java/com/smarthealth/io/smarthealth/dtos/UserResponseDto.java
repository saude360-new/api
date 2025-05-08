package com.smarthealth.io.smarthealth.dtos;



import java.time.LocalDateTime;
import java.util.List;

public class UserResponseDto{

  private String userId;
  private String firstName;
  private String lastName;
  private String emailAddress;
  private LocalDateTime createdAt;
  private List<UserMetadataDto> metadata;

  
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
  public String getEmailAddress() {
    return emailAddress;
  }
  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }
  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
  public List<UserMetadataDto> getMetadata() {
    return metadata;
  }
  public void setMetadata(List<UserMetadataDto> metadata) {
    this.metadata = metadata;
  }

  
  

  


}