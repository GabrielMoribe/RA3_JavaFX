package com.example.demo.entidades.arquivo;

import com.example.demo.entidades.Album;

import java.io.*;
import java.util.ArrayList;

public class AlbumFile {

    private static final String CAMINHO_ARQUIVO = "Data/albums.dat";

    public static void salvarLista(ArrayList<Album> lista_albums) {
        try {
            File arq = new File(CAMINHO_ARQUIVO);
            if (!arq.exists()) {
                arq.getParentFile().mkdirs();
                arq.createNewFile();
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arq))) {
                oos.writeObject(lista_albums);
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar lista de álbuns: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static ArrayList<Album> lerLista() {
        ArrayList<Album> lista_albums = new ArrayList<>();

        try {
            File arq = new File(CAMINHO_ARQUIVO);            if (arq.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arq))) {
                    @SuppressWarnings("unchecked")
                    ArrayList<Album> temp = (ArrayList<Album>) ois.readObject();
                    lista_albums = temp;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao ler lista de álbuns: " + e.getMessage());
            e.printStackTrace();
        }
        return lista_albums;
    }    public static void adicionarAlbum(Album novoAlbum) {
        ArrayList<Album> lista_albums = lerLista();
        lista_albums.add(novoAlbum);
        salvarLista(lista_albums);
    }

    public static boolean verificarAlbumExistente(String titulo, String artista) {
        ArrayList<Album> lista_albums = lerLista();
        return lista_albums.stream()
                .anyMatch(album -> album.getTituloAlbum().equals(titulo)
                        && album.getArtistaPrincipal().equals(artista));
    }

    public static boolean verificarAlbumExistenteParaUsuario(String titulo, String artista, String emailUsuario) {
        ArrayList<Album> lista_albums = lerLista();
        return lista_albums.stream()
                .anyMatch(album -> album.getTituloAlbum().equals(titulo)
                        && album.getArtistaPrincipal().equals(artista)
                        && album.getProprietario() != null 
                        && album.getProprietario().getEmail().equals(emailUsuario));
    }

    public static boolean deletarAlbum(Album album) {
        ArrayList<Album> lista_albums = lerLista();

        boolean removido = lista_albums.removeIf(a ->
                a.getTituloAlbum().equals(album.getTituloAlbum()) &&
                        a.getArtistaPrincipal().equals(album.getArtistaPrincipal()));

        if (removido) {
            salvarLista(lista_albums);
        }

        return removido;
    }

    public static void excluirAlbunsDoUsuario(String emailUsuario) {
        ArrayList<Album> lista_albums = lerLista();
        lista_albums.removeIf(album -> emailUsuario.equals(album.getEmailProprietario()));
        salvarLista(lista_albums);
    }
}