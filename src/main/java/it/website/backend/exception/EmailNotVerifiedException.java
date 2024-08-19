package it.website.backend.exception;

public class EmailNotVerifiedException extends Exception {
    public EmailNotVerifiedException(String message) {
        super(message);
    }
}
