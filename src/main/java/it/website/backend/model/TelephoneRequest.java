package it.website.backend.model;

import it.website.backend.validation.ValidPhoneNumber;

import javax.validation.constraints.NotBlank;

public class TelephoneRequest {

    @NotBlank(message = "Telephone is required")
    @ValidPhoneNumber(message = "Telephone number is invalid")
    private String telephone;

    // Getters and Setters
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
