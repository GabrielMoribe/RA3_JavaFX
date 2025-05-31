package org.example.projeto_javafx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.projeto_javafx.entidades.arquivo.UserFile;
import org.example.projeto_javafx.entidades.user.User;

public class EditController {

    @FXML
    private TextField nomeField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField telefoneField;

    @FXML
    private Label mensagemLabel;

    private User usuarioOriginal;
    private PerfilController perfilController;

    public void setUsuario(User usuario, PerfilController perfilController) {
        this.usuarioOriginal = usuario;
        this.perfilController = perfilController;
        
        // Preencher os campos com os dados atuais
        nomeField.setText(usuario.getNome());
        emailField.setText(usuario.getEmail());
        telefoneField.setText(usuario.getTelefone());
        
        configurarFormatacaoTelefone();
    }

    @FXML
    protected void onSalvarClick(ActionEvent event) {
        if (validarCampos()) {
            try {
                String novoNome = nomeField.getText().trim();
                String novoEmail = emailField.getText().trim();
                String novoTelefone = telefoneField.getText().trim();

                // Verificar se o email já existe (exceto se for o mesmo usuário)
                if (!novoEmail.equals(usuarioOriginal.getEmail()) && 
                    UserFile.verificarUsuarioExistente(novoEmail)) {
                    mensagemLabel.setText("Email já está em uso por outro usuário!");
                    mensagemLabel.setTextFill(Color.RED);
                    return;
                }

                // Atualizar o usuário
                UserFile.editarUsuario(usuarioOriginal.getEmail(), novoNome, novoEmail, novoTelefone);
                
                // Atualizar a lista no controller do perfil
                perfilController.atualizarLista();
                
                // Fechar a modal
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();

            } catch (Exception e) {
                mensagemLabel.setText("Erro ao salvar: " + e.getMessage());
                mensagemLabel.setTextFill(Color.RED);
            }
        }
    }

    @FXML
    protected void onCancelarClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private boolean validarCampos() {
        StringBuilder erros = new StringBuilder();

        if (nomeField.getText().trim().isEmpty()) {
            erros.append("• Nome é obrigatório\n");
        } else if (nomeField.getText().trim().length() < 2) {
            erros.append("• Nome deve ter pelo menos 2 caracteres\n");
        }

        if (emailField.getText().trim().isEmpty()) {
            erros.append("• Email é obrigatório\n");
        } else if (!isEmailValido(emailField.getText().trim())) {
            erros.append("• Email deve ter um formato válido\n");
        }

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

    private void configurarFormatacaoTelefone() {
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