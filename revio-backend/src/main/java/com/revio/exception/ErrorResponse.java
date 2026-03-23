package com.revio.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standard error response format for all API errors.
 * Provides consistent JSON structure for error messages.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    // HTTP status code
    private int status;

    // Error message
    private String message;

    // Timestamp of error
    private LocalDateTime timestamp;

    // Request path that caused the error
    private String path;

    // Field validation errors (if applicable)
    private Map<String, String> validationErrors;
}
