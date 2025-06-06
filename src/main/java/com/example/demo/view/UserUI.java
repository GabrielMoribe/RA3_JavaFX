package com.example.demo.view;

import com.example.demo.entidades.User;
import com.example.demo.entidades.arquivo.UserFile;
import com.example.demo.services.CatalogoService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

public class UserUI {

    public static BorderPane criarPainelCrudUsuarios(CatalogoService catalogoService) {
        BorderPane painelPrincipalAba = new BorderPane();
        painelPrincipalAba.setPadding(new Insets(10));

        ListView<User> visualizadorDeListaUsuarios = new ListView<>(catalogoService.getListaDeUsuarios());

        // Campos do forms
        TextField campoNome = new TextField();
        campoNome.setPromptText("Nome Completo");   //  Placeholder
        TextField campoEmail = new TextField();
        campoEmail.setPromptText("Email");
        TextField campoTelefone = new TextField();
        campoTelefone.setPromptText("(99) 99999-9999");

        // Cria os botoes
        Button botaoAdicionar = new Button("Adicionar Usuário");
        Button botaoEditar = new Button("Editar Selecionado");
        Button botaoExcluir = new Button("Excluir Selecionado");
        Button botaoLimparCampos = new Button("Limpar Campos");

        // Monta o layout (Grid) da pagina
        GridPane painelFormulario = new GridPane();
        painelFormulario.setVgap(8);
        painelFormulario.setHgap(8);
        painelFormulario.add(new Label("Nome:"), 0, 0);
        painelFormulario.add(campoNome, 1, 0);
        painelFormulario.add(new Label("Email:"), 0, 1);
        painelFormulario.add(campoEmail, 1, 1);
        painelFormulario.add(new Label("Telefone:"), 0, 2);
        painelFormulario.add(campoTelefone, 1, 2);

        // Agrupa elementos
        HBox painelBotoesFormulario = new HBox(10, botaoAdicionar, botaoLimparCampos);
        painelBotoesFormulario.setAlignment(Pos.CENTER_LEFT);

        VBox painelSuperior = new VBox(15, painelFormulario, painelBotoesFormulario);

        HBox painelBotoesLista = new HBox(10, botaoEditar, botaoExcluir);
        painelBotoesLista.setAlignment(Pos.CENTER_RIGHT);

        // Posiciona os grupos de elementos
        painelPrincipalAba.setTop(painelSuperior);
        painelPrincipalAba.setCenter(visualizadorDeListaUsuarios);
        painelPrincipalAba.setBottom(painelBotoesLista);
        BorderPane.setMargin(painelBotoesLista, new Insets(10, 0, 0, 0));

        // Chama o metodo configurarFormatacaoTelefone(TextField campoTelefone)
        configurarFormatacaoTelefone(campoTelefone);

        // Add usuario
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

                if (!catalogoService.adicionarUsuario(new User(nome, email, telefone))){
                    mostrarAlerta("Erro ao cadastrar usuario" , "Email já cadastrado no sistema. Tente outro email.");
                }
                //catalogoService.adicionarUsuario(new User(nome, email, telefone));
                limparCamposUsuario(campoNome, campoEmail, campoTelefone);

            } catch (Exception e) {
                mostrarAlerta("Erro", "Erro ao adicionar usuário: " + e.getMessage());
            }
        });

        // Editar
        botaoEditar.setOnAction(evento -> {
            User usuarioSelecionado = visualizadorDeListaUsuarios.getSelectionModel().getSelectedItem();
            if (usuarioSelecionado != null) {
                abrirModalEdicao(usuarioSelecionado, catalogoService);
            } else {
                mostrarAlerta("Nenhuma Seleção", "Por favor, selecione um usuário para editar.");
            }
        });

        // Excluir
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

        // Limpar campos
        botaoLimparCampos.setOnAction(evento -> limparCamposUsuario(campoNome, campoEmail, campoTelefone));

        return painelPrincipalAba;
    }

    private static void abrirModalEdicao(User usuario, CatalogoService catalogoService) {
        Stage modalEdicao = new Stage();
        modalEdicao.setTitle("Editar Usuário");
        modalEdicao.initModality(Modality.APPLICATION_MODAL);
        modalEdicao.setResizable(false);

        // Campos de edição
        TextField campoNomeEdicao = new TextField(usuario.getNome());
        TextField campoEmailEdicao = new TextField(usuario.getEmail());
        TextField campoTelefoneEdicao = new TextField(usuario.getTelefone());

        // Chama o metodo configurarFormatacaoTelefone(TextField campoTelefone)
        configurarFormatacaoTelefone(campoTelefoneEdicao);

        //Layout do modal
        GridPane formularioEdicao = new GridPane();
        formularioEdicao.setVgap(10);
        formularioEdicao.setHgap(10);
        formularioEdicao.setPadding(new Insets(20));
        
        formularioEdicao.add(new Label("Nome:"), 0, 0);
        formularioEdicao.add(campoNomeEdicao, 1, 0);
        formularioEdicao.add(new Label("Email:"), 0, 1);
        formularioEdicao.add(campoEmailEdicao, 1, 1);
        formularioEdicao.add(new Label("Telefone:"), 0, 2);
        formularioEdicao.add(campoTelefoneEdicao, 1, 2);

        // Botões da modal
        Button botaoSalvar = new Button("Salvar");
        Button botaoCancelar = new Button("Cancelar");
        
        HBox painelBotoes = new HBox(10, botaoSalvar, botaoCancelar);
        painelBotoes.setAlignment(Pos.CENTER);
        painelBotoes.setPadding(new Insets(10, 20, 20, 20));

        // Layout principal da modal
        VBox layoutModal = new VBox(formularioEdicao, painelBotoes);
        
        // Salvar
        botaoSalvar.setOnAction(evento -> {
            try {
                String novoNome = campoNomeEdicao.getText().trim();
                String novoEmail = campoEmailEdicao.getText().trim();
                String novoTelefone = campoTelefoneEdicao.getText().trim();

                if (novoNome.isEmpty() || novoEmail.isEmpty() || novoTelefone.isEmpty()) {
                    mostrarAlerta("Erro de Entrada", "Todos os campos são obrigatórios.");
                    return;
                }

                if (!isEmailValido(novoEmail)) {
                    mostrarAlerta("Erro de Formato", "Email deve ter um formato válido.");
                    return;
                }

                if (!isTelefoneValido(novoTelefone)) {
                    mostrarAlerta("Erro de Formato", "Telefone deve ter um formato válido.");
                    return;
                }

                // Verificar se o novo email já existe (se for diferente do atual)
                if (!novoEmail.equals(usuario.getEmail()) && 
                    catalogoService.getListaDeUsuarios().stream()
                        .anyMatch(u -> u.getEmail().equals(novoEmail))) {
                    mostrarAlerta("Erro", "Já existe um usuário com este email.");
                    return;
                }

                // Atualizar o usuário
                usuario.setNome(novoNome);
                String emailOriginal = usuario.getEmail();
                usuario.setEmail(novoEmail);
                usuario.setTelefone(novoTelefone);

                //Chama o metodo editarUsuario(String emailOriginal, String novoNome, String novoEmail, String novoTelefone)
                UserFile.editarUsuario(emailOriginal, novoNome, novoEmail, novoTelefone);


                // Atualizar a lista na interface
                catalogoService.getListaDeUsuarios().set(
                    catalogoService.getListaDeUsuarios().indexOf(usuario), usuario);

                mostrarAlerta("Sucesso", "Usuário editado com sucesso!");
                modalEdicao.close();

            } catch (Exception ex) {
                mostrarAlerta("Erro", "Erro ao editar usuário: " + ex.getMessage());
            }
        });

        botaoCancelar.setOnAction(evento -> modalEdicao.close());


        Scene cenaModal = new Scene(layoutModal, 350, 200);
        modalEdicao.setScene(cenaModal);
        modalEdicao.showAndWait();
    }

    private static void configurarFormatacaoTelefone(TextField campoTelefone) {

        // Codigo roda toda vez que o campo muda (textProperty())
        campoTelefone.textProperty().addListener((observable, oldValue, newValue) -> {
            String numFiltrado = newValue.replaceAll("\\D", "");

            if (numFiltrado.length() > 11) {
                numFiltrado = numFiltrado.substring(0, 11);
            }

            //Chama a funcao formatarTelefone(String numeros)
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
        // A-Za-z0-9+_.- = Um ou mais caracteres
        // A-Za-z0-9.-   = Um ou mais caracteres
        // A-Za-z{2,}    = Qualquer caractere, pelo menos duas vezes
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