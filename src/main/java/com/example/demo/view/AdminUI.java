package com.example.demo.view;

import com.example.demo.entidades.User;
import com.example.demo.entidades.arquivo.UserFile;
import com.example.demo.services.CatalogoService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.Optional;

public class AdminUI {    public static BorderPane criarPainelAdmin(CatalogoService catalogoService) {
        BorderPane painelPrincipalAba = new BorderPane();
        painelPrincipalAba.setPadding(new Insets(10));

        ListView<User> visualizadorDeListaUsuarios = new ListView<>();
        carregarUsuarios(visualizadorDeListaUsuarios);

        // Configurar exibição da lista para não mostrar o admin
        visualizadorDeListaUsuarios.setCellFactory(listView -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                } else {
                    // Não mostrar o admin na lista
                    if ("admin@email.com".equals(user.getEmail())) {
                        setText(null);
                        setGraphic(null);
                        setVisible(false);
                        setManaged(false);
                    } else {
                        setText(user.getNome() + " - " + user.getEmail() + " - " + user.getTelefone());
                        setVisible(true);
                        setManaged(true);
                    }
                }
            }
        });

        // --- Botões ---
        Button botaoExcluir = new Button("Excluir Selecionado");

        // --- Layout dos Botões de Ação da Lista (Inferior) ---
        HBox painelBotoesLista = new HBox(10, botaoExcluir);
        painelBotoesLista.setAlignment(Pos.CENTER_RIGHT);

        // --- Organização do Painel Principal ---
        painelPrincipalAba.setCenter(visualizadorDeListaUsuarios);
        painelPrincipalAba.setBottom(painelBotoesLista);
        BorderPane.setMargin(painelBotoesLista, new Insets(10, 0, 0, 0));        // Ações dos botões
        botaoExcluir.setOnAction(evento -> {
            User usuarioSelecionado = visualizadorDeListaUsuarios.getSelectionModel().getSelectedItem();
            if (usuarioSelecionado != null && !"admin@email.com".equals(usuarioSelecionado.getEmail())) {
                if (confirmarExclusaoAdmin(usuarioSelecionado)) {
                    excluirUsuarioCompleto(usuarioSelecionado, catalogoService);
                    carregarUsuarios(visualizadorDeListaUsuarios);
                    mostrarAlerta("Sucesso", "Usuário excluído com sucesso!");
                }
            } else {
                mostrarAlerta("Nenhuma Seleção", "Por favor, selecione um usuário para excluir.");
            }
        });

        return painelPrincipalAba;
    }

    private static void carregarUsuarios(ListView<User> listaUsuarios) {
        listaUsuarios.getItems().clear();
        UserFile.lerLista().forEach(user -> {
            // Não adicionar o admin na lista
            if (!"admin@email.com".equals(user.getEmail())) {
                listaUsuarios.getItems().add(user);
            }
        });    }

    private static boolean confirmarExclusaoAdmin(User usuario) {
        Alert dialogoConfirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        dialogoConfirmacao.setTitle("Confirmar Exclusão");
        dialogoConfirmacao.setHeaderText("Excluir usuário: " + usuario.getNome());
        dialogoConfirmacao.setContentText(
            "ATENÇÃO: Esta ação irá excluir permanentemente o usuário e todos os seus dados (álbuns e músicas). " +
            "Esta ação não pode ser desfeita. Deseja continuar?"
        );

        ButtonType botaoConfirmar = new ButtonType("Sim, Excluir", ButtonBar.ButtonData.OK_DONE);
        ButtonType botaoCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialogoConfirmacao.getButtonTypes().setAll(botaoConfirmar, botaoCancelar);

        Optional<ButtonType> resultado = dialogoConfirmacao.showAndWait();
        return resultado.isPresent() && resultado.get() == botaoConfirmar;
    }

    private static void excluirUsuarioCompleto(User usuario, CatalogoService catalogoService) {
        try {
            // Excluir todas as músicas do usuário
            catalogoService.excluirTodasMusicasDoUsuario(usuario.getEmail());

            // Excluir todos os álbuns do usuário
            catalogoService.excluirTodosAlbunsDoUsuario(usuario.getEmail());

            // Excluir o usuário
            catalogoService.excluirUsuario(usuario);

        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao excluir usuário: " + e.getMessage());        }
    }

    private static void mostrarAlerta(String titulo, String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}
