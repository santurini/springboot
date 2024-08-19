package it.website.backend.model;

public class TokenData {
    private final String email;
    private final long expirationTime;

    public TokenData(String email, long expirationTime) {
        this.email = email;
        this.expirationTime = expirationTime;
    }

    public String getEmail() {
        return email;
    }

    public long getExpirationTime() {
        return expirationTime;
    }
}
