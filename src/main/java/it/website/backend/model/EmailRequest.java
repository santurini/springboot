package it.website.backend.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class EmailRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
