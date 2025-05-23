package com.smarthealth.io.smarthealth.models;

import jakarta.persistence.*;




  @Entity
  @Table(name = "user_metadata")
  public class UserMetadata{

    @Id
    @Column(name = "user_metadata_row_id",nullable = false,insertable = false,updatable = false)
    private int user_metadata_row_id;
    

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;


      public UserMetadata(){}

      public UserMetadata(User user){
        this.user = user;
      }

      public User getUser_id(){
        return this.user;
      }

      public void setUser_id(User user){
        this.user = user; 
      }
  }

  


