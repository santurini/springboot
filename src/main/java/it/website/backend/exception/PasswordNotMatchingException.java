package it.website.backend.exception;

public class PasswordNotMatchingException extends Exception {
    public PasswordNotMatchingException(String message) {
        super(message);
    }
}
