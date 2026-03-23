package com.revio.service;

import com.revio.dto.AuthResponse;
import com.revio.dto.LoginRequest;
import com.revio.dto.RegisterRequest;
import com.revio.exception.UserAlreadyExistsException;
import com.revio.exception.AuthenticationException;
import com.revio.exception.ResourceNotFoundException;
import com.revio.model.User;
import com.revio.repository.UserRepository;
import com.revio.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service layer for user-related operations.
 * 
 * Handles:
 * - User registration with validation
 * - User login with password verification
 * - JWT token generation
 * - User profile retrieval
 */
@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * Register a new user.
     * 
     * @param registerRequest containing name, email, and password
     * @return AuthResponse with user details and JWT token
     * @throws UserAlreadyExistsException if email already registered
     */
    public AuthResponse register(RegisterRequest registerRequest) {
        log.info("Registering new user with email: {}", registerRequest.getEmail());

        // Check if user already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            log.warn("Registration failed: Email already exists: {}", registerRequest.getEmail());
            throw new UserAlreadyExistsException(
                    "Email already registered. Please use a different email.");
        }

        // Create new user
        User user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Save user to database
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(savedUser.getId(), savedUser.getEmail());

        return AuthResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .token(token)
                .message("User registered successfully")
                .build();
    }

    /**
     * Authenticate user and generate JWT token.
     * 
     * @param loginRequest containing email and password
     * @return AuthResponse with user details and JWT token
     * @throws ResourceNotFoundException if user not found
     * @throws AuthenticationException if password is incorrect
     */
    public AuthResponse login(LoginRequest loginRequest) {
        log.info("Login attempt for email: {}", loginRequest.getEmail());

        // Find user by email
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        if (userOptional.isEmpty()) {
            log.warn("Login failed: User not found with email: {}", loginRequest.getEmail());
            throw new ResourceNotFoundException("User not found with email: " + loginRequest.getEmail());
        }

        User user = userOptional.get();

        // Verify password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.warn("Login failed: Invalid password for email: {}", loginRequest.getEmail());
            throw new AuthenticationException("Invalid email or password");
        }

        log.info("Login successful for user: {}", user.getId());

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail());

        return AuthResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .token(token)
                .message("Login successful")
                .build();    }    }

    /**
     * Get user by ID.
     * 
     * @param userId the user's ID
     * @return the User object
     * @throws ResourceNotFoundException if user not found
     */
    public User getUserById(String userId) {
        log.debug("Fetching user with ID: {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with ID: " + userId));
    }

    /**
     * Get user by email.
     * 
     * @param email the user's email
     * @return the User object
     * @throws ResourceNotFoundException if user not found
     */
    public User getUserByEmail(String email) {
        log.debug("Fetching user with email: {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with email: " + email));
    }

    /**
     * Verify if a token is valid and get the user ID from it.
     * 
     * @param token the JWT token
     * @return the user ID if token is valid
     * @throws AuthenticationException if token is invalid
     */
    public String verifyToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthenticationException("Invalid or expired token");
        }
        return jwtTokenProvider.getUserIdFromToken(token);
    }
}
