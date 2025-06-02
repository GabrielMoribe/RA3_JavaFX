package service;

import com.example.demo.Musica;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Album;


public class CatalogoService {
    private final ObservableList<Musica> listaDeMusicas = FXCollections.observableArrayList();
    private final ObservableList<Album> listaDeAlbuns = FXCollections.observableArrayList();

    // Métodos para Musicas
    public ObservableList<Musica> getListaDeMusicas() {
        return listaDeMusicas;
    }

    public void adicionarMusica(Musica musica) {
        if (musica != null && musica.getTituloMusica() != null && !musica.getTituloMusica().trim().isEmpty()) {
            listaDeMusicas.add(musica);
        }
    }

    public void excluirMusica(Musica musica) {
        if (musica != null) {
            listaDeMusicas.remove(musica);
        }
    }

    // Métodos para Albuns
    public ObservableList<Album> getListaDeAlbuns() {
        return listaDeAlbuns;
    }

    public void adicionarAlbum(Album album) {
        if (album != null && album.getTituloAlbum() != null && !album.getTituloAlbum().trim().isEmpty()) {
            listaDeAlbuns.add(album);
        }
    }

    public void excluirAlbum(Album album) {
        if (album != null) {
            listaDeAlbuns.remove(album);
        }
    }
}