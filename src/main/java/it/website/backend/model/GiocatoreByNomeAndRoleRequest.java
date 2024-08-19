package it.website.backend.model;

public class GiocatoreByNomeAndRoleRequest {
    private String nome;
    private String r;

    // Getters and Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }
}
