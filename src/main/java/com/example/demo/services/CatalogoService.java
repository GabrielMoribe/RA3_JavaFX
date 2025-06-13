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
    private final ObservableList<User> listaDeUsuarios = FXCollections.observableArrayList();    public CatalogoService() {
        carregarDados();
    }    private void carregarDados() {
        // Carregar usuários primeiro
        ArrayList<User> usuariosSalvos = UserFile.lerLista();
        listaDeUsuarios.setAll(usuariosSalvos);

        // Carregar álbuns - eles já devem ter as dependências de usuário
        ArrayList<Album> albunsSalvos = AlbumFile.lerLista();
        listaDeAlbuns.setAll(albunsSalvos);

        // Carregar músicas - elas já devem ter as dependências de álbum
        ArrayList<Musica> musicasSalvas = MusicaFile.lerLista();
        listaDeMusicas.setAll(musicasSalvas);
    }    public void filtrarDadosUsuarioLogado(String emailUsuario) {
        // Filtrar álbuns do usuário logado
        ArrayList<Album> todosAlbuns = AlbumFile.lerLista();
        ArrayList<Album> albunsUsuario = new ArrayList<>();
        for (Album album : todosAlbuns) {
            if (album.getProprietario() != null && emailUsuario.equals(album.getProprietario().getEmail())) {
                albunsUsuario.add(album);
            }
        }
        listaDeAlbuns.setAll(albunsUsuario);

        // Filtrar músicas do usuário logado
        ArrayList<Musica> todasMusicas = MusicaFile.lerLista();
        ArrayList<Musica> musicasUsuario = new ArrayList<>();
        for (Musica musica : todasMusicas) {
            if (musica.getAlbum() != null && 
                musica.getAlbum().getProprietario() != null && 
                emailUsuario.equals(musica.getAlbum().getProprietario().getEmail())) {
                musicasUsuario.add(musica);
            }
        }
        listaDeMusicas.setAll(musicasUsuario);
    }



        // Métodos para Musicas
    public ObservableList<Musica> getListaDeMusicas() {
        return listaDeMusicas;
    }    public void adicionarMusica(Musica musica) {
        if (musica != null && musica.getTituloMusica() != null && !musica.getTituloMusica().trim().isEmpty()) {
            if (!MusicaFile.verificarMusicaExistente(musica.getTituloMusica(), musica.getArtista())) {
                listaDeMusicas.add(musica);
                MusicaFile.adicionarMusica(musica);
            }
        }
    }
    
    // Método para adicionar música com injeção de dependência
    public void adicionarMusicaComDependencia(String tituloMusica, String artista, int ano, Album album) {
        if (album != null) {
            Musica musica = criarMusica(tituloMusica, artista, ano, album);
            adicionarMusica(musica);
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
    }    public void adicionarAlbum(Album album) {
        if (album != null && album.getTituloAlbum() != null && !album.getTituloAlbum().trim().isEmpty()) {
            if (!AlbumFile.verificarAlbumExistente(album.getTituloAlbum(), album.getArtistaPrincipal())) {
                listaDeAlbuns.add(album);
                AlbumFile.adicionarAlbum(album);
            }
        }
    }
    
    // Método para adicionar álbum com injeção de dependência
    public void adicionarAlbumComDependencia(String tituloAlbum, String artistaPrincipal, int anoLancamento, String genero) {
        User proprietario = AuthService.getUsuarioLogado();
        if (proprietario != null) {
            Album album = criarAlbum(tituloAlbum, artistaPrincipal, anoLancamento, genero, proprietario);
            adicionarAlbum(album);
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
    }    public void excluirTodasMusicasDoUsuario(String emailUsuario) {
        // Remover da lista em memória
        listaDeMusicas.removeIf(musica -> musica.getAlbum() != null && 
                                        musica.getAlbum().getProprietario() != null &&
                                        emailUsuario.equals(musica.getAlbum().getProprietario().getEmail()));
        
        // Atualizar arquivo
        MusicaFile.excluirMusicasDoUsuario(emailUsuario);
    }

    public void excluirTodosAlbunsDoUsuario(String emailUsuario) {
        // Remover da lista em memória
        listaDeAlbuns.removeIf(album -> album.getProprietario() != null &&
                                       emailUsuario.equals(album.getProprietario().getEmail()));
        
        // Atualizar arquivo
        AlbumFile.excluirAlbunsDoUsuario(emailUsuario);
    }public void atualizarNomeAlbumEmMusicas(String nomeAntigoAlbum, String novoNomeAlbum, String emailUsuario) {
        // Atualizar músicas em memória
        for (Musica musica : listaDeMusicas) {
            if (emailUsuario.equals(musica.getEmailProprietario()) && 
                musica.getAlbum() != null && 
                nomeAntigoAlbum.equals(musica.getAlbum().getTituloAlbum())) {
                musica.getAlbum().setTituloAlbum(novoNomeAlbum);
            }
        }
        
        // Salvar no arquivo
        MusicaFile.atualizarNomeAlbumEmMusicas(nomeAntigoAlbum, novoNomeAlbum, emailUsuario);
    }    public void atualizarAlbum(Album album) {
        if (album != null) {
            // O álbum já foi modificado na referência, apenas salva no arquivo
            AlbumFile.salvarLista(new ArrayList<>(listaDeAlbuns));
        }
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

    // Métodos auxiliares para injeção de dependência
    public User buscarUsuarioPorEmail(String email) {
        return listaDeUsuarios.stream()                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }
    
    // Método para criar Album com injeção de dependência
    public Album criarAlbum(String tituloAlbum, String artistaPrincipal, int anoLancamento, String genero, User proprietario) {
        return new Album(tituloAlbum, artistaPrincipal, anoLancamento, genero, proprietario);
    }
    
    // Método para criar Musica com injeção de dependência
    public Musica criarMusica(String tituloMusica, String artista, int ano, Album album) {
        return new Musica(tituloMusica, artista, ano, album);
    }    // Método auxiliar para buscar álbum pelo nome para o usuário logado
    public Album buscarAlbumPorNome(String nomeAlbum) {
        User usuarioLogado = AuthService.getUsuarioLogado();
        if (usuarioLogado == null) return null;
        
        return listaDeAlbuns.stream()
                .filter(album -> album.getTituloAlbum().equals(nomeAlbum) && 
                               album.getProprietario() != null &&
                               album.getProprietario().getEmail().equals(usuarioLogado.getEmail()))
                .findFirst()
                .orElse(null);
    }    // Método para recarregar apenas os dados do usuário logado
    public void recarregarDadosUsuarioLogado() {
        if (AuthService.getUsuarioLogado() != null) {
            String emailUsuario = AuthService.getUsuarioLogado().getEmail();
            filtrarDadosUsuarioLogado(emailUsuario);
        }
    }
}