package com.example.demo.entidades;

import java.io.Serializable;

public class Musica implements Serializable {
    private static final long serialVersionUID = 1L;

    private String tituloMusica;
    private String artista;
    private String nomeAlbum;
    private int ano;

    public Musica(String tituloMusica, String artista, String nomeAlbum, int ano) {
        this.tituloMusica = tituloMusica;
        this.artista = artista;
        this.nomeAlbum = nomeAlbum;
        this.ano = ano;
    }

    public String getTituloMusica() {
        return tituloMusica;
    }

    public void setTituloMusica(String tituloMusica) {
        this.tituloMusica = tituloMusica;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getNomeAlbum() {
        return nomeAlbum;
    }

    public void setNomeAlbum(String nomeAlbum) {
        this.nomeAlbum = nomeAlbum;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    @Override
    public String toString() {
        return tituloMusica + " - " + artista + " (Álbum: " + nomeAlbum + ", " + ano + ")";
    }
}