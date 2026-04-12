package com.uniwayfinder.notification.exception;

// Custom exception for resource not found scenarios
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}