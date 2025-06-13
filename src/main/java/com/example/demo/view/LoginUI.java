package com.example.demo.view;

import com.example.demo.services.AuthService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginUI {

    public static boolean mostrarTelaLogin(Stage palcoPrincipal) {
        Stage loginStage = new Stage();
        loginStage.setTitle("Login - Sistema de Catálogo Musical");
        loginStage.setResizable(false);

        // Campos de login
        TextField campoEmail = new TextField();
        campoEmail.setPromptText("Email");
        campoEmail.setPrefWidth(250);

        PasswordField campoSenha = new PasswordField();
        campoSenha.setPromptText("Senha");
        campoSenha.setPrefWidth(250);

        // Botões
        Button botaoLogin = new Button("Entrar");
        botaoLogin.setPrefWidth(100);
        Button botaoCadastro = new Button("Cadastrar");
        botaoCadastro.setPrefWidth(100);
        Button botaoSair = new Button("Sair");
        botaoSair.setPrefWidth(100);

        // Labels
        Label tituloLogin = new Label("Faça seu Login");
        tituloLogin.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label mensagemStatus = new Label("");
        mensagemStatus.setStyle("-fx-text-fill: red;");

        // Layout
        VBox layoutPrincipal = new VBox(15);
        layoutPrincipal.setAlignment(Pos.CENTER);
        layoutPrincipal.setPadding(new Insets(30));

        GridPane formulario = new GridPane();
        formulario.setVgap(10);
        formulario.setHgap(10);
        formulario.setAlignment(Pos.CENTER);

        formulario.add(new Label("Email:"), 0, 0);
        formulario.add(campoEmail, 1, 0);
        formulario.add(new Label("Senha:"), 0, 1);
        formulario.add(campoSenha, 1, 1);

        HBox painelBotoes = new HBox(10);
        painelBotoes.setAlignment(Pos.CENTER);
        painelBotoes.getChildren().addAll(botaoLogin, botaoCadastro, botaoSair);

        layoutPrincipal.getChildren().addAll(tituloLogin, formulario, painelBotoes, mensagemStatus);

        // Variável para controlar se o login foi bem-sucedido
        final boolean[] loginSucesso = {false};

        // Ação do botão Login
        botaoLogin.setOnAction(evento -> {
            String email = campoEmail.getText().trim();
            String senha = campoSenha.getText();

            if (email.isEmpty() || senha.isEmpty()) {
                mensagemStatus.setText("Por favor, preencha todos os campos.");
                return;
            }

            if (AuthService.login(email, senha)) {
                loginSucesso[0] = true;
                loginStage.close();
            } else {
                mensagemStatus.setText("Email ou senha incorretos.");
                campoSenha.clear();
            }
        });

        // Ação do botão Cadastro (Abre a tela de cadastro)
        botaoCadastro.setOnAction(evento -> {
            if (mostrarTelaCadastro()) {
                loginSucesso[0] = true;
                loginStage.close();
            }
        });

        // Ação do botão Sair
        botaoSair.setOnAction(evento -> {
            loginStage.close();
            System.exit(0);
        });

        // Permitir Enter para fazer login
        campoSenha.setOnAction(evento -> botaoLogin.fire());

        Scene cena = new Scene(layoutPrincipal, 400, 300);
        loginStage.setScene(cena);
        loginStage.showAndWait();

        return loginSucesso[0];
    }

    public static boolean mostrarTelaCadastro() {
        Stage cadastroStage = new Stage();
        cadastroStage.setTitle("Cadastro - Sistema de Catálogo Musical");
        cadastroStage.setResizable(false);

        // Campos de cadastro
        TextField campoNome = new TextField();
        campoNome.setPromptText("Nome Completo");
        campoNome.setPrefWidth(250);

        TextField campoEmail = new TextField();
        campoEmail.setPromptText("Email");
        campoEmail.setPrefWidth(250);

        TextField campoTelefone = new TextField();
        campoTelefone.setPromptText("(99) 99999-9999");
        campoTelefone.setPrefWidth(250);

        PasswordField campoSenha = new PasswordField();
        campoSenha.setPromptText("Senha");
        campoSenha.setPrefWidth(250);

        PasswordField campoConfirmarSenha = new PasswordField();
        campoConfirmarSenha.setPromptText("Confirmar Senha");
        campoConfirmarSenha.setPrefWidth(250);

        // Botões
        Button botaoCadastrar = new Button("Cadastrar");
        botaoCadastrar.setPrefWidth(100);
        Button botaoVoltar = new Button("Voltar");
        botaoVoltar.setPrefWidth(100);

        // Labels
        Label tituloCadastro = new Label("Criar Nova Conta");
        tituloCadastro.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label mensagemStatus = new Label("");
        mensagemStatus.setStyle("-fx-text-fill: red;");

        // Layout
        VBox layoutPrincipal = new VBox(15);
        layoutPrincipal.setAlignment(Pos.CENTER);
        layoutPrincipal.setPadding(new Insets(30));

        GridPane formulario = new GridPane();
        formulario.setVgap(10);
        formulario.setHgap(10);
        formulario.setAlignment(Pos.CENTER);

        formulario.add(new Label("Nome:"), 0, 0);
        formulario.add(campoNome, 1, 0);
        formulario.add(new Label("Email:"), 0, 1);
        formulario.add(campoEmail, 1, 1);
        formulario.add(new Label("Telefone:"), 0, 2);
        formulario.add(campoTelefone, 1, 2);
        formulario.add(new Label("Senha:"), 0, 3);
        formulario.add(campoSenha, 1, 3);
        formulario.add(new Label("Confirmar Senha:"), 0, 4);
        formulario.add(campoConfirmarSenha, 1, 4);

        HBox painelBotoes = new HBox(10);
        painelBotoes.setAlignment(Pos.CENTER);
        painelBotoes.getChildren().addAll(botaoCadastrar, botaoVoltar);

        layoutPrincipal.getChildren().addAll(tituloCadastro, formulario, painelBotoes, mensagemStatus);

        // Configurar formatação do telefone
        configurarFormatacaoTelefone(campoTelefone);

        // Variável para se o cadastro foi bem-sucedido
        final boolean[] cadastroSucesso = {false};

        // Ação do botão Cadastrar
        botaoCadastrar.setOnAction(evento -> {
            String nome = campoNome.getText().trim();
            String email = campoEmail.getText().trim();
            String telefone = campoTelefone.getText().trim();
            String senha = campoSenha.getText();
            String confirmarSenha = campoConfirmarSenha.getText();

            // Validações
            if (nome.isEmpty() || email.isEmpty() || telefone.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
                mensagemStatus.setText("Por favor, preencha todos os campos.");
                return;
            }

            if (!isEmailValido(email)) {
                mensagemStatus.setText("Email deve ter um formato válido.");
                return;
            }

            if (!isTelefoneValido(telefone)) {
                mensagemStatus.setText("Telefone deve ter um formato válido.");
                return;
            }

            if (senha.length() < 6) {
                mensagemStatus.setText("A senha deve ter pelo menos 6 caracteres.");
                return;
            }

            if (!senha.equals(confirmarSenha)) {
                mensagemStatus.setText("As senhas não coincidem.");
                return;
            }

            if (AuthService.cadastrar(nome, email, telefone, senha)) {
                cadastroSucesso[0] = true;
                cadastroStage.close();
            } else {
                mensagemStatus.setText("Já existe um usuário com este email.");
            }
        });

        // Ação do botão Voltar
        botaoVoltar.setOnAction(evento -> cadastroStage.close());

        Scene cena = new Scene(layoutPrincipal, 450, 400);
        cadastroStage.setScene(cena);
        cadastroStage.showAndWait();

        return cadastroSucesso[0];
    }

    private static void configurarFormatacaoTelefone(TextField campoTelefone) {
        campoTelefone.textProperty().addListener((observable, oldValue, newValue) -> {
            String numFiltrado = newValue.replaceAll("\\D", "");

            if (numFiltrado.length() > 11) {
                numFiltrado = numFiltrado.substring(0, 11);
            }

            String formatted = formatarTelefone(numFiltrado);

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
}
