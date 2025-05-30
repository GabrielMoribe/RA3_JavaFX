package org.example.projeto_javafx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {

    @FXML
    private TextField nomeField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField senhaField;


    @FXML
    private TextField telefoneField;

    @FXML
    private Label mensagemLabel;

    @FXML
    protected void onCadastrarButtonClick(ActionEvent event) {
        if (validarCampos()) {
            try {
                // Obter os dados dos campos
                String nome = nomeField.getText().trim();
                String email = emailField.getText().trim();
                String telefone = telefoneField.getText().trim();

                // Carregar a tela de dashboard
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/projeto_javafx/perfil-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 500, 400);

                // Obter o controller da tela de dashboard e passar os dados
                PerfilController perfilController = fxmlLoader.getController();
                perfilController.setDadosUsuario(nome, email, telefone);

                // Obter o stage atual e trocar a cena
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setTitle("Dashboard - Sistema de Cadastro");
                stage.setScene(scene);

            } catch (IOException e) {
                mensagemLabel.setText("Erro ao carregar a próxima tela: " + e.getMessage());
                mensagemLabel.setTextFill(Color.RED);
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void onLimparButtonClick() {
        limparCampos();
        mensagemLabel.setText("");
    }

    private boolean validarCampos() {
        StringBuilder erros = new StringBuilder();

        // Validar nome
        if (nomeField.getText().trim().isEmpty()) {
            erros.append("• Nome é obrigatório\n");
        } else if (nomeField.getText().trim().length() < 2) {
            erros.append("• Nome deve ter pelo menos 2 caracteres\n");
        }

        // Validar email
        if (emailField.getText().trim().isEmpty()) {
            erros.append("• Email é obrigatório\n");
        } else if (!isEmailValido(emailField.getText().trim())) {
            erros.append("• Email deve ter um formato válido\n");
        }

        // Validar senha
        if (senhaField.getText().isEmpty()) {
            erros.append("• Senha é obrigatória\n");
        } else if (senhaField.getText().length() < 6) {
            erros.append("• Senha deve ter pelo menos 6 caracteres\n");
        }


        // Validar telefone
        if (telefoneField.getText().trim().isEmpty()) {
            erros.append("• Telefone é obrigatório\n");
        } else if (!isTelefoneValido(telefoneField.getText().trim())) {
            erros.append("• Telefone deve ter um formato válido\n");
        }

        if (erros.length() > 0) {
            mensagemLabel.setText("Erros encontrados:\n" + erros.toString());
            mensagemLabel.setTextFill(Color.RED);
            return false;
        }

        return true;
    }

    private boolean isEmailValido(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    }

    private boolean isTelefoneValido(String telefone) {
        String apenasNumeros = telefone.replaceAll("\\D", "");
        return apenasNumeros.length() >= 10 && apenasNumeros.length() <= 11;
    }

    private void limparCampos() {
        nomeField.clear();
        emailField.clear();
        senhaField.clear();
        telefoneField.clear();
    }

    @FXML
    private void initialize() {
        telefoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            String apenasNumeros = newValue.replaceAll("\\D", "");

            if (apenasNumeros.length() > 11) {
                apenasNumeros = apenasNumeros.substring(0, 11);
            }

            String formatted = formatarTelefone(apenasNumeros);

            if (!newValue.equals(formatted)) {
                telefoneField.setText(formatted);
            }
        });
    }

    private String formatarTelefone(String numeros) {
        if (numeros.length() == 0) return "";
        if (numeros.length() <= 2) return "(" + numeros;
        if (numeros.length() <= 6) return "(" + numeros.substring(0, 2) + ") " + numeros.substring(2);
        if (numeros.length() <= 10) return "(" + numeros.substring(0, 2) + ") " + numeros.substring(2, 6) + "-" + numeros.substring(6);
        return "(" + numeros.substring(0, 2) + ") " + numeros.substring(2, 7) + "-" + numeros.substring(7);
    }
}