package com.smarthealth.io.smarthealth.v1.controllers;

import org.springframework.web.bind.annotation.*; 


@RestController
@RequestMapping("/api/v1")
public class TestRoute {

  @GetMapping("/test")
  public String getStatus() {
    return "SmartHealth API is running";
  }
}
