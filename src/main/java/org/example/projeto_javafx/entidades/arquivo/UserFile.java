package org.example.projeto_javafx.entidades.arquivo;

import org.example.projeto_javafx.entidades.user.User;

import java.io.*;
import java.util.ArrayList;

public class UserFile {

    private static final String CAMINHO_ARQUIVO = "Data/users.dat";

    //SALVA A LISTA NO ARQUIVO "users.dat"
    public static void salvarLista(ArrayList<User> lista_usuarios) {
        try {
            File arq = new File(CAMINHO_ARQUIVO);
            if (!arq.exists()) {
                arq.createNewFile();
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arq))) {
                oos.writeObject(lista_usuarios);
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar lista: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Lê a lista de usuários do arquivo
    public static ArrayList<User> lerLista() {
        ArrayList<User> lista_usuarios = new ArrayList<>();

        try {
            File arq = new File(CAMINHO_ARQUIVO);
            if (arq.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arq))) {
                    lista_usuarios = (ArrayList<User>) ois.readObject();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao ler lista: " + e.getMessage());
            e.printStackTrace();
        }
        return lista_usuarios;
    }

    public static void adicionarPessoa(User newUser) {
        ArrayList<User> lista_usuarios = lerLista();

        //VERIFICA SE O EMAIL JA ESTA CADASTRADO
        boolean usuarioExiste = lista_usuarios.stream()
                .anyMatch(user -> user.getEmail().equals(newUser.getEmail()));

        if (!usuarioExiste) {
            lista_usuarios.add(newUser);
            salvarLista(lista_usuarios);
        } else {
            System.err.println("Usuário com este email já existe!");
        }
    }

    public static boolean verificarUsuarioExistente(String email) {
        ArrayList<User> lista_usuarios = lerLista();
        return lista_usuarios.stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    public static void editarUsuario(String emailOriginal, String novoNome, String novoEmail, String novoTelefone) {
        ArrayList<User> lista_usuarios = lerLista();
        
        for (User user : lista_usuarios) {
            if (user.getEmail().equals(emailOriginal)) {
                user.setNome(novoNome);
                user.setEmail(novoEmail);
                user.setTelefone(novoTelefone);
                break;
            }
        }
        
        salvarLista(lista_usuarios);
    }

    public static User buscarUsuarioPorEmail(String email) {
        ArrayList<User> lista_usuarios = lerLista();
        return lista_usuarios.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }
}