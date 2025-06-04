package com.example.demo.services;

import com.example.demo.entidades.Musica;
import com.example.demo.entidades.User;
import com.example.demo.entidades.arquivo.AlbumFile;
import com.example.demo.entidades.arquivo.MusicaFile;
import com.example.demo.entidades.arquivo.UserFile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.example.demo.entidades.Album;

import java.util.ArrayList;

public class CatalogoService {
    private final ObservableList<Musica> listaDeMusicas = FXCollections.observableArrayList();
    private final ObservableList<Album> listaDeAlbuns = FXCollections.observableArrayList();
    private final ObservableList<User> listaDeUsuarios = FXCollections.observableArrayList();

    public CatalogoService() {
        carregarDados();
    }

    private void carregarDados() {
        // Carregar músicas do arquivo
        ArrayList<Musica> musicasSalvas = MusicaFile.lerLista();
        listaDeMusicas.setAll(musicasSalvas);

        // Carregar álbuns do arquivo
        ArrayList<Album> albunsSalvos = AlbumFile.lerLista();
        listaDeAlbuns.setAll(albunsSalvos);

        // Carregar usuários do arquivo
        ArrayList<User> usuariosSalvos = UserFile.lerLista();
        listaDeUsuarios.setAll(usuariosSalvos);
    }

    // Métodos para Musicas
    public ObservableList<Musica> getListaDeMusicas() {
        return listaDeMusicas;
    }

    public void adicionarMusica(Musica musica) {
        if (musica != null && musica.getTituloMusica() != null && !musica.getTituloMusica().trim().isEmpty()) {
            if (!MusicaFile.verificarMusicaExistente(musica.getTituloMusica(), musica.getArtista())) {
                listaDeMusicas.add(musica);
                MusicaFile.adicionarMusica(musica);
            }
        }
    }

    public void excluirMusica(Musica musica) {
        if (musica != null) {
            listaDeMusicas.remove(musica);
            MusicaFile.deletarMusica(musica);
        }
    }

    // Métodos para Albuns
    public ObservableList<Album> getListaDeAlbuns() {
        return listaDeAlbuns;
    }

    public void adicionarAlbum(Album album) {
        if (album != null && album.getTituloAlbum() != null && !album.getTituloAlbum().trim().isEmpty()) {
            if (!AlbumFile.verificarAlbumExistente(album.getTituloAlbum(), album.getArtistaPrincipal())) {
                listaDeAlbuns.add(album);
                AlbumFile.adicionarAlbum(album);
            }
        }
    }

    public void excluirAlbum(Album album) {
        if (album != null) {
            listaDeAlbuns.remove(album);
            AlbumFile.deletarAlbum(album);
        }
    }

    // Métodos para Usuários
    public ObservableList<User> getListaDeUsuarios() {
        return listaDeUsuarios;
    }

    public void adicionarUsuario(User usuario) {
        if (usuario != null && usuario.getNome() != null && !usuario.getNome().trim().isEmpty()) {
            if (!UserFile.verificarUsuarioExistente(usuario.getEmail())) {
                listaDeUsuarios.add(usuario);
                UserFile.adicionarPessoa(usuario);
            }
        }
    }

    public void excluirUsuario(User usuario) {
        if (usuario != null) {
            listaDeUsuarios.remove(usuario);
            UserFile.deletarUsuario(usuario.getEmail());
        }
    }
}