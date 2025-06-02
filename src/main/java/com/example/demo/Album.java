package model;

import java.util.concurrent.atomic.AtomicInteger;

public class Album {
    private static final AtomicInteger idContador = new AtomicInteger(0);
    private int id;
    private String tituloAlbum;
    private String artistaPrincipal;
    private int anoLancamento;
    private String genero;

    public Album(String tituloAlbum, String artistaPrincipal, int anoLancamento, String genero) {
        this.id = idContador.incrementAndGet();
        this.tituloAlbum = tituloAlbum;
        this.artistaPrincipal = artistaPrincipal;
        this.anoLancamento = anoLancamento;
        this.genero = genero;
    }

    public int getId() {
        return id;
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

    @Override
    public String toString() {
        return tituloAlbum + " - " + artistaPrincipal + " (" + anoLancamento + ") [" + genero + "]";
    }
}