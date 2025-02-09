package com.smarthealth.io.smarthealth.v1.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; 


@RestController
@RequestMapping("/api/v1")
public class TestRoute {

  @GetMapping("/test")
  public ResponseEntity<String> getStatus() {
    final HttpHeaders headers = new HttpHeaders();

    headers.add("Connection", "close");
    headers.add("Content-Type", "application/json; charset=UTF-8");
    headers.add("Cache-Control", "no-cache,no-store,max-age=0");

    return new ResponseEntity<String>(
      "{\"message\":\"SmartHealth API is running\"}",
      headers,
      HttpStatus.OK
    );
  }
}
