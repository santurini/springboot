package it.website.backend.model;

import javax.validation.constraints.NotBlank;

public class UsernameRequest {

    @NotBlank(message = "Username is required")
    private String username;

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
