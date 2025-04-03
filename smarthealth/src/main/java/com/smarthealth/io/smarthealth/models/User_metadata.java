package com.smarthealth.io.smarthealth.models;

import jakarta.persistence.*;




  @Entity
  @Table(name = "user_metadata")
  public class User_metadata{

     
      
    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;


      public User_metadata(){}

      public User_metadata(User user){
        this.user = user;
      }

      public User getUser_id(){
        return this.user;
      }

      public void setUser_id(User user){
        this.user = user; 
      }
  }

  


