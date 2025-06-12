package com.example.demo.entidades;

import java.io.Serializable;

public class Album implements Serializable {
    private static final long serialVersionUID = 1L;

    private String tituloAlbum;
    private String artistaPrincipal;
    private int anoLancamento;
    private String genero;
    private String emailProprietario; // Email do usuário proprietário

    public Album(String tituloAlbum, String artistaPrincipal, int anoLancamento, String genero) {
        this.tituloAlbum = tituloAlbum;
        this.artistaPrincipal = artistaPrincipal;
        this.anoLancamento = anoLancamento;
        this.genero = genero;
        this.emailProprietario = "";
    }

    public Album(String tituloAlbum, String artistaPrincipal, int anoLancamento, String genero, String emailProprietario) {
        this.tituloAlbum = tituloAlbum;
        this.artistaPrincipal = artistaPrincipal;
        this.anoLancamento = anoLancamento;
        this.genero = genero;
        this.emailProprietario = emailProprietario;
    }

    public String getTituloAlbum() {
        return tituloAlbum;
    }

    public void setTituloAlbum(String tituloAlbum) {
        this.tituloAlbum = tituloAlbum;
    }

    public String getArtistaPrincipal() {
        return artistaPrincipal;
    }

    public void setArtistaPrincipal(String artistaPrincipal) {
        this.artistaPrincipal = artistaPrincipal;
    }

    public int getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(int anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getEmailProprietario() {
        return emailProprietario;
    }

    public void setEmailProprietario(String emailProprietario) {
        this.emailProprietario = emailProprietario;
    }

    @Override
    public String toString() {
        return tituloAlbum + " - " + artistaPrincipal + " (" + anoLancamento + ") [" + genero + "]";
    }
}