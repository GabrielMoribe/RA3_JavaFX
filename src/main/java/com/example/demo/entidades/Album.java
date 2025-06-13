package com.example.demo.entidades;

import java.io.Serializable;

public class Album implements Serializable {
    private static final long serialVersionUID = 2L; // Incrementando para nova versão

    private String tituloAlbum;
    private String artistaPrincipal;
    private int anoLancamento;
    private String genero;
    private User proprietario; // Instância do usuário proprietário

    // Construtor principal com injeção de dependência
    public Album(String tituloAlbum, String artistaPrincipal, int anoLancamento, String genero, User proprietario) {
        this.tituloAlbum = tituloAlbum;
        this.artistaPrincipal = artistaPrincipal;
        this.anoLancamento = anoLancamento;
        this.genero = genero;
        this.proprietario = proprietario;
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
    }    public User getProprietario() {
        return proprietario;
    }

    public void setProprietario(User proprietario) {
        this.proprietario = proprietario;
    }    // Método de compatibilidade para email do proprietário
    public String getEmailProprietario() {
        return proprietario != null ? proprietario.getEmail() : "";
    }

    @Override
    public String toString() {
        return tituloAlbum + " - " + artistaPrincipal + " (" + anoLancamento + ") [" + genero + "]";
    }
}