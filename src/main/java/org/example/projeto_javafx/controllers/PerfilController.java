package org.example.projeto_javafx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import org.example.projeto_javafx.entidades.arquivo.UserFile;
import org.example.projeto_javafx.entidades.user.User;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class PerfilController implements Initializable {

    @FXML
    private Label nomeLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label telefoneLabel;

    @FXML
    private ListView<String> usuariosListView;

    // DADOS PRINTADOS NA TELA
    private String nomeUsuario;
    private String emailUsuario;
    private String telefoneUsuario;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        carregarListaUsuarios();
    }

    // SETTAR OS DADOS DO ULTIMO USUARIO CADASTRADO
    public void setDadosUsuario(String nome, String email, String telefone) {
        this.nomeUsuario = nome;
        this.emailUsuario = email;
        this.telefoneUsuario = telefone;

        // Atualizar os labels com os dados
        nomeLabel.setText(nome);
        emailLabel.setText(email);
        telefoneLabel.setText(telefone);
    }

    //CHAMA lerLista() PARA LISTAR TODOS OS USUARIOS CADASTRADOS
    private void carregarListaUsuarios() {
        try {
            ArrayList<User> usuarios = UserFile.lerLista();
            ObservableList<String> usuariosFormatados = FXCollections.observableArrayList();

            for (User user : usuarios) {
                String userInfo = String.format("%s - %s - %s", 
                    user.getNome(), 
                    user.getEmail(), 
                    user.getTelefone());
                usuariosFormatados.add(userInfo);
            }

            usuariosListView.setItems(usuariosFormatados);
        } catch (Exception e) {
            System.err.println("Erro ao carregar lista de usuários: " + e.getMessage());
        }
    }

    @FXML
    protected void onNovoCadastroClick(ActionEvent event) {
        try {
            //CHAMA UMA NOVA TELA DE CADASTRO
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/projeto_javafx/hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 450, 600);

            // Obter o stage atual
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Sistema de Cadastro");
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onSairClick(ActionEvent event) {
        // Fechar a aplicação
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onEditarUsuarioClick(ActionEvent event) {
        String selectedItem = usuariosListView.getSelectionModel().getSelectedItem();
        
        if (selectedItem == null) {

            System.out.println("Selecione um usuário para editar");
            return;
        }
        
        // Extrair o email do item selecionado (assumindo formato: "Nome - Email - Telefone")
        String[] partes = selectedItem.split(" - ");
        if (partes.length >= 2) {
            String email = partes[1];
            User usuario = UserFile.buscarUsuarioPorEmail(email);
            
            if (usuario != null) {
                abrirModalEdicao(usuario);
            }
        }
    }

    private void abrirModalEdicao(User usuario) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/projeto_javafx/editar-view-modal.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            
            EditController controller = fxmlLoader.getController();
            controller.setUsuario(usuario, this);
            
            Stage modalStage = new Stage();
            modalStage.setTitle("Editar Usuário");
            modalStage.setScene(scene);
            modalStage.setResizable(false);
            modalStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            modalStage.showAndWait();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void atualizarLista() {
        carregarListaUsuarios();
    }

    @FXML
    protected void onDeletarUsuarioClick(ActionEvent event) {
        String selectedItem = usuariosListView.getSelectionModel().getSelectedItem();
        
        if (selectedItem == null) {
            System.out.println("Selecione um usuário para deletar");
            return;
        }
        
        // Extrair o email do item selecionado
        String[] partes = selectedItem.split(" - ");
        if (partes.length >= 2) {
            String email = partes[1];
            String nome = partes[0];
            
            // Confirmar a exclusão
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Exclusão");
            alert.setHeaderText("Deletar Usuário");
            alert.setContentText("Tem certeza que deseja deletar o usuário " + nome + "?");
            
            alert.showAndWait().ifPresent(response -> {
                if (response == javafx.scene.control.ButtonType.OK) {
                    boolean deletado = UserFile.deletarUsuario(email);
                    if (deletado) {
                        System.out.println("Usuário deletado com sucesso!");
                        atualizarLista(); // Atualizar a lista na tela
                    } else {
                        System.out.println("Erro ao deletar usuário");
                    }
                }
            });
        }
    }
}