package com.example.pokemon;

public class Pokemon {
    private String nome;
    private String url;
    private int id;


    public String getNome() {
        return this.nome;
    }

    public String getUrl() {
        return url;
    }

    public int getId() {
        return id;
    }


    public Pokemon(String nome, String url, int id) {
        this.nome = nome;
        this.url = url;
        this.id = id;
    }

    @Override
    public String toString() {
        return "> Nome: " + nome + "\n"
                + "> Url: " + url + "\n"
                + "> Id: " + id + "\n";
    }
}
