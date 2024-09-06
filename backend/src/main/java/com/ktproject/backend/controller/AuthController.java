package com.ktproject.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ktproject.backend.entity.User;
import com.ktproject.backend.service.UserService;
import com.ktproject.backend.util.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private UserService userService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtUtil jwtUtil;

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody User user) {
    if (userService.findByUsername(user.getUsername()).isPresent()) {
      return ResponseEntity.badRequest().body("Username is already taken");
    }
    userService.saveUser(user, passwordEncoder);
    return ResponseEntity.ok("User registered successfully");
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody User user) {
    try {
      @SuppressWarnings("unused")
      Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
    
      String token = jwtUtil.generateToken(user.getUsername());
      return ResponseEntity.ok().body(new AuthResponse(token));
    } catch (Exception e) {
      System.err.println(e);
      return ResponseEntity.status(401).body("Invalid username/password");
    }
  }
}
