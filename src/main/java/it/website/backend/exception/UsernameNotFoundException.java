package it.website.backend.exception;

public class UsernameNotFoundException extends Exception {
    public UsernameNotFoundException(String message) {
        super(message);
    }
}