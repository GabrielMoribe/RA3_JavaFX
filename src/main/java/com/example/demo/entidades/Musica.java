package com.example.demo.entidades;

import java.io.Serializable;

public class Musica implements Serializable {
    private static final long serialVersionUID = 2L; // Incrementando para nova versão

    private String tituloMusica;
    private String artista;
    private int ano;
    private Album album; // Instância do álbum

    // Construtor principal com injeção de dependência
    public Musica(String tituloMusica, String artista, int ano, Album album) {
        this.tituloMusica = tituloMusica;
        this.artista = artista;
        this.ano = ano;
        this.album = album;
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
    }    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    // Métodos de compatibilidade para nome do álbum
    public String getNomeAlbum() {
        return album != null ? album.getTituloAlbum() : "";
    }

    public void setNomeAlbum(String nomeAlbum) {
        // Este método será usado para compatibilidade, mas a injeção de dependência deveria ser preferida
        // Se não há album, não podemos definir o nome
        if (album != null) {
            album.setTituloAlbum(nomeAlbum);
        }
    }

    public int getAno() {
        return ano;
    }    public void setAno(int ano) {
        this.ano = ano;
    }

    // Método de compatibilidade para email do proprietário (via álbum)
    public String getEmailProprietario() {
        return album != null && album.getProprietario() != null ? album.getProprietario().getEmail() : "";
    }

    @Override
    public String toString() {
        String nomeAlbum = album != null ? album.getTituloAlbum() : "Sem álbum";
        return tituloMusica + " - " + artista + " (Álbum: " + nomeAlbum + ", " + ano + ")";
    }
}