package com.example.demo.entidades.arquivo;

import com.example.demo.entidades.Musica;

import java.io.*;
import java.util.ArrayList;

public class MusicaFile {

    private static final String CAMINHO_ARQUIVO = "Data/musicas.dat";

    public static void salvarLista(ArrayList<Musica> lista_musicas) {
        try {
            File arq = new File(CAMINHO_ARQUIVO);
            if (!arq.exists()) {
                arq.getParentFile().mkdirs();
                arq.createNewFile();
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arq))) {
                oos.writeObject(lista_musicas);
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar lista de músicas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static ArrayList<Musica> lerLista() {
        ArrayList<Musica> lista_musicas = new ArrayList<>();

        try {
            File arq = new File(CAMINHO_ARQUIVO);
            if (arq.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arq))) {
                    lista_musicas = (ArrayList<Musica>) ois.readObject();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao ler lista de músicas: " + e.getMessage());
            e.printStackTrace();
        }
        return lista_musicas;
    }

    public static void adicionarMusica(Musica novaMusica) {
        ArrayList<Musica> lista_musicas = lerLista();

        boolean musicaExiste = lista_musicas.stream()
                .anyMatch(musica -> musica.getTituloMusica().equals(novaMusica.getTituloMusica())
                        && musica.getArtista().equals(novaMusica.getArtista()));

        if (!musicaExiste) {
            lista_musicas.add(novaMusica);
            salvarLista(lista_musicas);
        } else {
            System.err.println("Música já existe!");
        }
    }

    public static boolean verificarMusicaExistente(String titulo, String artista) {
        ArrayList<Musica> lista_musicas = lerLista();
        return lista_musicas.stream()
                .anyMatch(musica -> musica.getTituloMusica().equals(titulo)
                        && musica.getArtista().equals(artista));
    }

    public static boolean deletarMusica(Musica musica) {
        ArrayList<Musica> lista_musicas = lerLista();

        boolean removido = lista_musicas.removeIf(m ->
                m.getTituloMusica().equals(musica.getTituloMusica()) &&
                        m.getArtista().equals(musica.getArtista()));

        if (removido) {
            salvarLista(lista_musicas);
        }

        return removido;
    }
}