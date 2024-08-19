package it.website.backend.model;

import org.springframework.data.annotation.Id;

import java.util.List;

public class Asta {

    @Id
    private String id;
    private String player;
    private double price;
    private List<String> users; // Store as List<String>

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
