package com.example.app.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")   // temporarily allow all origins
public class HelloController {
  @GetMapping("/hello")
  public String sayHello() {
    return "Hello, Spring Boot!";
  }
}