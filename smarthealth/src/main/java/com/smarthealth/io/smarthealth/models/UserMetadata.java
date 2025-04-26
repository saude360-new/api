package com.smarthealth.io.smarthealth.models;

import jakarta.persistence.*;




  @Entity
  @Table(name = "user_metadata")
  public class UserMetadata{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_metadata_row_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "metadata_key", nullable = false)
    private String key;

    @Column(name = "metadata_value", nullable = false)
    private String value;


      public UserMetadata(){}


      public Long getId() {
        return id;
      }


      public void setId(Long id) {
        this.id = id;
      }


      public User getUser() {
        return user;
      }


      public void setUser(User user) {
        this.user = user;
      }


      public String getKey() {
        return key;
      }


      public void setKey(String key) {
        this.key = key;
      }


      public String getValue() {
        return value;
      }


      public void setValue(String value) {
        this.value = value;
      }

      


      
  }

  


