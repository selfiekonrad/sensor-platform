package com.ceniuch.common.exceptions;

/**
 * Raised when the upstream authentication backend (sensor-management-service)
 * cannot be reached, times out, or responds with an unexpected/server error.
 * Callers should surface this as HTTP 503 (Service Unavailable) since it is a
 * transient, retryable condition rather than a client error.
 */
public class AuthServiceUnavailableException extends RuntimeException {

    public AuthServiceUnavailableException(String message) {
        super(message);
    }

    public AuthServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
