package it.website.backend.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "giocatori")
public class Giocatore {

    @Field("Id")
    private String id;

    @Field("Nome")
    private String nome;

    @Field("Squadra")
    private String squadra;

    @Field("R")
    private String r;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String Nome) {
        this.nome = nome;
    }

    public String getSquadra() {
        return squadra;
    }

    public void setSquadra(String squadra) {
        this.squadra = squadra;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }
}