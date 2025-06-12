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
import java.util.stream.Collectors;

public class CatalogoService {
    private final ObservableList<Musica> listaDeMusicas = FXCollections.observableArrayList();
    private final ObservableList<Album> listaDeAlbuns = FXCollections.observableArrayList();
    private final ObservableList<User> listaDeUsuarios = FXCollections.observableArrayList();    public CatalogoService() {
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

    public void filtrarDadosUsuarioLogado(String emailUsuario) {
        // Filtrar músicas do usuário logado
        ArrayList<Musica> todasMusicas = MusicaFile.lerLista();
        ArrayList<Musica> musicasUsuario = todasMusicas.stream()
                .filter(musica -> emailUsuario.equals(musica.getEmailProprietario()))
                .collect(Collectors.toCollection(ArrayList::new));
        listaDeMusicas.setAll(musicasUsuario);

        // Filtrar álbuns do usuário logado
        ArrayList<Album> todosAlbuns = AlbumFile.lerLista();
        ArrayList<Album> albunsUsuario = todosAlbuns.stream()
                .filter(album -> emailUsuario.equals(album.getEmailProprietario()))
                .collect(Collectors.toCollection(ArrayList::new));
        listaDeAlbuns.setAll(albunsUsuario);
    }



        // Métodos para Musicas
    public ObservableList<Musica> getListaDeMusicas() {
        return listaDeMusicas;
    }
    
    public void adicionarMusica(Musica musica) {
        if (musica != null && musica.getTituloMusica() != null && !musica.getTituloMusica().trim().isEmpty()) {
            // Definir o proprietário como o usuário logado
            musica.setEmailProprietario(AuthService.getUsuarioLogado().getEmail());
            
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
            // Definir o proprietário como o usuário logado
            album.setEmailProprietario(AuthService.getUsuarioLogado().getEmail());
            
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
    
    public boolean adicionarUsuario(User usuario) {
        if (usuario != null && usuario.getNome() != null && !usuario.getNome().trim().isEmpty()) {
            if (!UserFile.verificarUsuarioExistente(usuario.getEmail())) {
                listaDeUsuarios.add(usuario);
                UserFile.adicionarUsuario(usuario);
                return true;
            }
        }
        return false;
    }

    public void excluirUsuario(User usuario) {
        if (usuario != null) {
            listaDeUsuarios.remove(usuario);
            UserFile.deletarUsuario(usuario.getEmail());
        }
    }

    public void excluirTodasMusicasDoUsuario(String emailUsuario) {
        // Remover da lista em memória
        listaDeMusicas.removeIf(musica -> emailUsuario.equals(musica.getEmailProprietario()));
        
        // Atualizar arquivo
        MusicaFile.excluirMusicasDoUsuario(emailUsuario);
    }

    public void excluirTodosAlbunsDoUsuario(String emailUsuario) {
        // Remover da lista em memória
        listaDeAlbuns.removeIf(album -> emailUsuario.equals(album.getEmailProprietario()));
        
        // Atualizar arquivo
        AlbumFile.excluirAlbunsDoUsuario(emailUsuario);
    }

    public void atualizarNomeAlbumEmMusicas(String nomeAntigoAlbum, String novoNomeAlbum, String emailUsuario) {
        // Atualizar músicas em memória
        listaDeMusicas.stream()
            .filter(musica -> emailUsuario.equals(musica.getEmailProprietario()))
            .filter(musica -> nomeAntigoAlbum.equals(musica.getNomeAlbum()))
            .forEach(musica -> musica.setNomeAlbum(novoNomeAlbum));
        
        // Salvar no arquivo
        MusicaFile.atualizarNomeAlbumEmMusicas(nomeAntigoAlbum, novoNomeAlbum, emailUsuario);
    }

    public void atualizarAlbum(Album album) {
        if (album != null) {
            // O álbum já foi modificado na referência, apenas salva no arquivo
            AlbumFile.salvarLista(new ArrayList<>(listaDeAlbuns));
        }
    }

    public void atualizarDadosUsuarioLogado() {
        String emailUsuario = AuthService.getUsuarioLogado().getEmail();
        filtrarDadosUsuarioLogado(emailUsuario);
    }    public void atualizarUsuario(User usuario) {
        atualizarUsuario(usuario, null);
    }
    
    public void atualizarUsuario(User usuario, String emailOriginal) {
        if (usuario != null) {
            // Recarregar todas as listas do arquivo para garantir dados atualizados
            ArrayList<User> todosUsuarios = UserFile.lerLista();
            
            // Se emailOriginal foi fornecido, usar ele para identificar o usuário
            // Caso contrário, usa o email do usuário logado
            String emailParaBuscar = emailOriginal != null ? emailOriginal : AuthService.getUsuarioLogado().getEmail();
            
            // Atualizar na lista completa
            for (int i = 0; i < todosUsuarios.size(); i++) {
                User usuarioExistente = todosUsuarios.get(i);
                // Identificar o usuário correto pelo email original
                if (usuarioExistente.getEmail().equals(emailParaBuscar)) {
                    todosUsuarios.set(i, usuario);
                    break;
                }
            }
            
            // Atualizar lista em memória
            listaDeUsuarios.clear();
            listaDeUsuarios.addAll(todosUsuarios);
            
            // Salvar no arquivo
            UserFile.salvarLista(todosUsuarios);
            
            // Atualizar a sessão do usuário logado se for o mesmo usuário
            if (AuthService.getUsuarioLogado() != null && 
                AuthService.getUsuarioLogado().getEmail().equals(emailParaBuscar)) {
                AuthService.atualizarUsuarioLogado(usuario);
            }
        }
    }
}