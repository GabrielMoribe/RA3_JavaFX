package com.example.demo;


import java.util.concurrent.atomic.AtomicInteger;

public class Musica {
    private static final AtomicInteger idContador = new AtomicInteger(0);
    private int id;
    private String tituloMusica;
    private String artista;
    private String nomeAlbum; // Nome do álbum onde a música está
    private int ano;

    public Musica(String tituloMusica, String artista, String nomeAlbum, int ano) {
        this.id = idContador.incrementAndGet();
        this.tituloMusica = tituloMusica;
        this.artista = artista;
        this.nomeAlbum = nomeAlbum;
        this.ano = ano;
    }

    public int getId() {
        return id;
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



