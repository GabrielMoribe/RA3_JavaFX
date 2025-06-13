package com.example.demo.services;

import com.example.demo.entidades.User;
import com.example.demo.entidades.arquivo.UserFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class AuthService {
    private static User usuarioLogado = null; 
    
       public static boolean login(String email, String senha) {
        try {
            // Verificar se é admin
            if ("admin@email.com".equals(email) && "123123".equals(senha)) {
                usuarioLogado = new User("Administrador", email, "", hashSenha(senha));
                return true;
            }
            
            String senhaHasheada = hashSenha(senha);
            User usuario = UserFile.buscarUsuarioPorEmail(email);
            
            if (usuario != null && senhaHasheada.equals(usuario.getSenha())) {
                usuarioLogado = usuario;
                return true;
            }
        } catch (Exception e) {
            System.err.println("Erro durante login: " + e.getMessage());
        }
        return false;
    }

    public static boolean cadastrar(String nome, String email, String telefone, String senha) {
        try {
            String senhaHasheada = hashSenha(senha);
            User novoUsuario = new User(nome, email, telefone, senhaHasheada);
            
            if (UserFile.adicionarUsuario(novoUsuario)) {
                usuarioLogado = novoUsuario;
                return true;
            }
        } catch (Exception e) {
            System.err.println("Erro durante cadastro: " + e.getMessage());
        }
        return false;
    }

    public static void logout() {
        usuarioLogado = null;
    }

    public static User getUsuarioLogado() {
        return usuarioLogado;
    }

    public static boolean isLogado() {
        return usuarioLogado != null;
    }

    public static boolean isAdmin() {
        return usuarioLogado != null && "admin@email.com".equals(usuarioLogado.getEmail());
    }    private static String hashSenha(String senha) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(senha.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao hashear senha", e);
        }
    }
    
    public static String criptografarSenha(String senha) {
        return hashSenha(senha);    }

    public static void atualizarUsuarioLogado(User usuarioAtualizado) {
        if (usuarioLogado != null && usuarioAtualizado != null) {
            usuarioLogado = usuarioAtualizado;
        }
    }
}
