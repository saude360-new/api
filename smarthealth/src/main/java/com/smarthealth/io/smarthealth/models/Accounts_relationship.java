package com.smarthealth.io.smarthealth.models;

import jakarta.persistence.*;

@Entity
@Table(name = "accounts_relationship")
public class Accounts_relationship {

  @Id
  @GeneratedValue( strategy = GenerationType.IDENTITY)
      @Column(name = "accounts_relationship_row_id")
      private int accounts_relationship_row_id;

  @OneToMany
  @JoinColumn(name = "device_id", nullable = false, unique = true)
  private Devices devices;

  @OneToMany
  @JoinColumn(name = "caregiver_id", nullable = false, unique = true)
  private User caregiver;

  @OneToMany
  @JoinColumn(name = "patient_id", nullable = false, unique = true)
  private User patient;
  
}
