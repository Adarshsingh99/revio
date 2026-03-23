package com.crevio.controller;

import com.revio.dto.AuthResponse;
import com.revio.dto.LoginRequest;
import com.revio.dto.RegisterRequest;
import com.revio.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller for Revio API.
 * 
 * Endpoints:
 * - POST /auth/register - Register a new user
 * - POST /auth/login - Login with email and password
 * 
 * Uses JWT tokens for subsequent authenticated requests.
 */
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * Register a new user.
     * 
     * Request body:
     * {
     *   "name": "John Doe",
     *   "email": "john@example.com",
     *   "password": "securePassword123"
     * }
     * 
     * @param registerRequest containing name, email, and password
     * @return ResponseEntity with AuthResponse containing JWT token
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("Registration request for email: {}", registerRequest.getEmail());
        AuthResponse response = userService.register(registerRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Login user with email and password.
     * 
     * Request body:
     * {
     *   "email": "john@example.com",
     *   "password": "securePassword123"
     * }
     * 
     * @param loginRequest containing email and password
     * @return ResponseEntity with AuthResponse containing JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login request for email: {}", loginRequest.getEmail());
        AuthResponse response = userService.login(loginRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Health check endpoint to verify API is running.
     * 
     * @return simple status message
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return new ResponseEntity<>("Revio API is running!", HttpStatus.OK);
    }
}
