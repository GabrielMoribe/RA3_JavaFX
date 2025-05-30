package org.example.projeto_javafx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.IOException;

public class PerfilController {

    @FXML
    private Label nomeLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label telefoneLabel;

    // Dados do usuário que serão passados da tela anterior
    private String nomeUsuario;
    private String emailUsuario;
    private String telefoneUsuario;

    // Método para definir os dados do usuário
    public void setDadosUsuario(String nome, String email, String telefone) {
        this.nomeUsuario = nome;
        this.emailUsuario = email;
        this.telefoneUsuario = telefone;

        // Atualizar os labels com os dados
        nomeLabel.setText(nome);
        emailLabel.setText(email);
        telefoneLabel.setText(telefone);
    }

    @FXML
    protected void onNovoCadastroClick(ActionEvent event) {
        try {
            // Carregar a tela de cadastro novamente
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
}