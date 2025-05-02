package com.smarthealth.io.smarthealth.models;

import jakarta.persistence.*;

@Entity
@Table(name = "accounts_relationship")
public class AccountsRelationship {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "accounts_relationship_row_id")
  private int accounts_relationship_row_id;



  @ManyToOne
  @JoinColumn(name = "caregiver_id", nullable = false, unique = true)
  private User caregiver;

  @ManyToOne
  @JoinColumn(name = "patient_id", nullable = false, unique = true)
  private User patient;

  public int getAccounts_relationship_row_id() {
    return accounts_relationship_row_id;
  }

  public void setAccounts_relationship_row_id(int accounts_relationship_row_id) {
    this.accounts_relationship_row_id = accounts_relationship_row_id;
  }

  public User getCaregiver() {
    return caregiver;
  }

  public void setCaregiver(User caregiver) {
    this.caregiver = caregiver;
  }

  public User getPatient() {
    return patient;
  }

  public void setPatient(User patient) {
    this.patient = patient;
  }

 

  
}
