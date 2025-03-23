package com.smarthealth.io.smarthealth.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

//import org.springframework.boot.autoconfigure.security.SecurityProperties.User;


  @Entity
  @Table(name = "user")
  public class User{

      @Id
      @GeneratedValue( strategy = GenerationType.UUID)
      @Column(name = "user_id")
      private String user_id;

      @Column(name = "first_name", nullable = false)
      private String first_name;

      @Column(name = "last_name", nullable = false)
      private String last_name;

      @Column(name = "birth_date", nullable = false)
      private LocalDateTime birth_date;

      @Column(name = "gender", nullable = false)
      private String gender;

      @Column(name = "email_address", nullable = false)
      private String email_address;

      @Column(name = "email_hash", nullable = false)
      private String email_hash;

      @Column(name = "password_digest", nullable = false)
      private String password_digest;

      @Column(name = "salt", nullable = false, unique = true)
      private String salt;

      @Column(name = "user_role", nullable = false)
      private String user_role;

      @Column(name = "symmetric_key", nullable = false)
      private String symmetric_key;

      @Column(name = "public_key", nullable = false)
      private String public_key;

      @Column(name = "private_key", nullable = false)
      private String private_key;

      @Column(name = "created_at", nullable = false, updatable = false)
      private LocalDateTime created_at;

          //eu acho que isso precisa poder ser nulo, se não ao criar vc tem que colocar algo
      @Column(name = "updated_at"/* , nullable = false*/)
      private LocalDateTime updated_at;

  

      public User() {
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
      }



      public String getUser_id() {
        return user_id;
      }



      public void setUser_id(String user_id) {
        this.user_id = user_id;
      }



      public String getFirst_name() {
        return first_name;
      }



      public void setFirst_name(String first_name) {
        this.first_name = first_name;
      }



      public String getLast_name() {
        return last_name;
      }



      public void setLast_name(String last_name) {
        this.last_name = last_name;
      }



      public LocalDateTime getBirth_date() {
        return birth_date;
      }



      public void setBirth_date(LocalDateTime birth_date) {
        this.birth_date = birth_date;
      }



      public String getGender() {
        return gender;
      }



      public void setGender(String gender) {
        this.gender = gender;
      }



      public String getEmail_address() {
        return email_address;
      }



      public void setEmail_address(String email_address) {
        this.email_address = email_address;
      }



      public String getEmail_hash() {
        return email_hash;
      }



      public void setEmail_hash(String email_hash) {
        this.email_hash = email_hash;
      }



      public String getPassword_digest() {
        return password_digest;
      }



      public void setPassword_digest(String password_digest) {
        this.password_digest = password_digest;
      }



      public String getSalt() {
        return salt;
      }



      public void setSalt(String salt) {
        this.salt = salt;
      }



      public String getUser_role() {
        return user_role;
      }



      public void setUser_role(String user_role) {
        this.user_role = user_role;
      }



      public String getSymmetric_key() {
        return symmetric_key;
      }



      public void setSymmetric_key(String symmetric_key) {
        this.symmetric_key = symmetric_key;
      }



      public String getPublic_key() {
        return public_key;
      }



      public void setPublic_key(String public_key) {
        this.public_key = public_key;
      }



      public String getPrivate_key() {
        return private_key;
      }



      public void setPrivate_key(String private_key) {
        this.private_key = private_key;
      }



      public LocalDateTime getCreated_at() {
        return created_at;
      }



      public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
      }



      public LocalDateTime getUpdated_at() {
        return updated_at;
      }



      public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
      }


  
  

  


  
}

