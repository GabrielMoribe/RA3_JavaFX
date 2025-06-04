package com.example.demo.view;

import com.example.demo.entidades.User;
import com.example.demo.services.CatalogoService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class UserUI {

    public static BorderPane criarPainelCrudUsuarios(CatalogoService catalogoService) {
        BorderPane painelPrincipalAba = new BorderPane();
        painelPrincipalAba.setPadding(new Insets(10));

        ListView<User> visualizadorDeListaUsuarios = new ListView<>(catalogoService.getListaDeUsuarios());

        TextField campoNome = new TextField();
        campoNome.setPromptText("Nome Completo");
        TextField campoEmail = new TextField();
        campoEmail.setPromptText("Email");
        TextField campoTelefone = new TextField();
        campoTelefone.setPromptText("(99) 99999-9999");

        Button botaoAdicionar = new Button("Adicionar Usuário");
        Button botaoExcluir = new Button("Excluir Selecionado");
        Button botaoLimparCampos = new Button("Limpar Campos");

        GridPane painelFormulario = new GridPane();
        painelFormulario.setVgap(8);
        painelFormulario.setHgap(8);
        painelFormulario.add(new Label("Nome:"), 0, 0);
        painelFormulario.add(campoNome, 1, 0);
        painelFormulario.add(new Label("Email:"), 0, 1);
        painelFormulario.add(campoEmail, 1, 1);
        painelFormulario.add(new Label("Telefone:"), 0, 2);
        painelFormulario.add(campoTelefone, 1, 2);

        HBox painelBotoesFormulario = new HBox(10, botaoAdicionar, botaoLimparCampos);
        painelBotoesFormulario.setAlignment(Pos.CENTER_LEFT);

        VBox painelSuperior = new VBox(15, painelFormulario, painelBotoesFormulario);

        HBox painelBotoesLista = new HBox(10, botaoExcluir);
        painelBotoesLista.setAlignment(Pos.CENTER_RIGHT);

        painelPrincipalAba.setTop(painelSuperior);
        painelPrincipalAba.setCenter(visualizadorDeListaUsuarios);
        painelPrincipalAba.setBottom(painelBotoesLista);
        BorderPane.setMargin(painelBotoesLista, new Insets(10, 0, 0, 0));

        // Configurar formatação do telefone
        configurarFormatacaoTelefone(campoTelefone);

        botaoAdicionar.setOnAction(evento -> {
            try {
                String nome = campoNome.getText().trim();
                String email = campoEmail.getText().trim();
                String telefone = campoTelefone.getText().trim();

                if (nome.isEmpty() || email.isEmpty() || telefone.isEmpty()) {
                    mostrarAlerta("Erro de Entrada", "Todos os campos são obrigatórios.");
                    return;
                }

                if (!isEmailValido(email)) {
                    mostrarAlerta("Erro de Formato", "Email deve ter um formato válido.");
                    return;
                }

                if (!isTelefoneValido(telefone)) {
                    mostrarAlerta("Erro de Formato", "Telefone deve ter um formato válido.");
                    return;
                }

                catalogoService.adicionarUsuario(new User(nome, email, telefone));
                limparCamposUsuario(campoNome, campoEmail, campoTelefone);
            } catch (Exception ex) {
                mostrarAlerta("Erro", "Erro ao adicionar usuário: " + ex.getMessage());
            }
        });

        botaoLimparCampos.setOnAction(evento -> limparCamposUsuario(campoNome, campoEmail, campoTelefone));

        botaoExcluir.setOnAction(evento -> {
            User usuarioSelecionado = visualizadorDeListaUsuarios.getSelectionModel().getSelectedItem();
            if (usuarioSelecionado != null) {
                if (confirmarExclusao("Excluir Usuário: " + usuarioSelecionado.getNome(),
                        "Tem certeza que deseja excluir este usuário?")) {
                    catalogoService.excluirUsuario(usuarioSelecionado);
                    limparCamposUsuario(campoNome, campoEmail, campoTelefone);
                }
            } else {
                mostrarAlerta("Nenhuma Seleção", "Por favor, selecione um usuário para excluir.");
            }
        });

        return painelPrincipalAba;
    }

    private static void configurarFormatacaoTelefone(TextField campoTelefone) {
        campoTelefone.textProperty().addListener((observable, oldValue, newValue) -> {
            String apenasNumeros = newValue.replaceAll("\\D", "");

            if (apenasNumeros.length() > 11) {
                apenasNumeros = apenasNumeros.substring(0, 11);
            }

            String formatted = formatarTelefone(apenasNumeros);

            if (!newValue.equals(formatted)) {
                campoTelefone.setText(formatted);
            }
        });
    }

    private static String formatarTelefone(String numeros) {
        if (numeros.length() == 0) return "";
        if (numeros.length() <= 2) return "(" + numeros;
        if (numeros.length() <= 6) return "(" + numeros.substring(0, 2) + ") " + numeros.substring(2);
        if (numeros.length() <= 10) return "(" + numeros.substring(0, 2) + ") " + numeros.substring(2, 6) + "-" + numeros.substring(6);
        return "(" + numeros.substring(0, 2) + ") " + numeros.substring(2, 7) + "-" + numeros.substring(7);
    }

    private static boolean isEmailValido(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    }

    private static boolean isTelefoneValido(String telefone) {
        String apenasNumeros = telefone.replaceAll("\\D", "");
        return apenasNumeros.length() >= 10 && apenasNumeros.length() <= 11;
    }

    private static void limparCamposUsuario(TextField campoNome, TextField campoEmail, TextField campoTelefone) {
        campoNome.clear();
        campoEmail.clear();
        campoTelefone.clear();
        campoNome.requestFocus();
    }

    private static void mostrarAlerta(String titulo, String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    private static boolean confirmarExclusao(String tituloCabecalho, String mensagemConteudo) {
        Alert dialogoConfirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        dialogoConfirmacao.setTitle("Confirmar Exclusão");
        dialogoConfirmacao.setHeaderText(tituloCabecalho);
        dialogoConfirmacao.setContentText(mensagemConteudo);
        Optional<ButtonType> resultado = dialogoConfirmacao.showAndWait();
        return resultado.isPresent() && resultado.get() == ButtonType.OK;
    }
}