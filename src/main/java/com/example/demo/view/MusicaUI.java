package com.example.demo.view;

import com.example.demo.entidades.Musica;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import com.example.demo.services.CatalogoService;

import java.util.Optional;

public class MusicaUI {

    public static BorderPane criarPainelCrudMusicas(CatalogoService catalogoService) {
        BorderPane painelPrincipalAba = new BorderPane();
        painelPrincipalAba.setPadding(new Insets(10));

        ListView<Musica> visualizadorDeListaMusicas = new ListView<>(catalogoService.getListaDeMusicas());

        TextField campoTituloMusica = new TextField();
        campoTituloMusica.setPromptText("Título da Música");
        TextField campoArtista = new TextField();
        campoArtista.setPromptText("Artista");
        TextField campoNomeAlbum = new TextField();
        campoNomeAlbum.setPromptText("Nome do Álbum");
        TextField campoAno = new TextField();
        campoAno.setPromptText("Ano (ex: 2023)");

        Button botaoAdicionar = new Button("Adicionar Música");
        Button botaoExcluir = new Button("Excluir Selecionada");
        Button botaoLimparCampos = new Button("Limpar Campos");

        GridPane painelFormulario = new GridPane();
        painelFormulario.setVgap(8);
        painelFormulario.setHgap(8);
        painelFormulario.add(new Label("Título:"), 0, 0);
        painelFormulario.add(campoTituloMusica, 1, 0);
        painelFormulario.add(new Label("Artista:"), 0, 1);
        painelFormulario.add(campoArtista, 1, 1);
        painelFormulario.add(new Label("Álbum:"), 0, 2);
        painelFormulario.add(campoNomeAlbum, 1, 2);
        painelFormulario.add(new Label("Ano:"), 0, 3);
        painelFormulario.add(campoAno, 1, 3);


        HBox painelBotoesFormulario = new HBox(10, botaoAdicionar, botaoLimparCampos);
        painelBotoesFormulario.setAlignment(Pos.CENTER_LEFT);

        VBox painelSuperior = new VBox(15, painelFormulario, painelBotoesFormulario);

        HBox painelBotoesLista = new HBox(10, botaoExcluir);
        painelBotoesLista.setAlignment(Pos.CENTER_RIGHT);

        painelPrincipalAba.setTop(painelSuperior);
        painelPrincipalAba.setCenter(visualizadorDeListaMusicas);
        painelPrincipalAba.setBottom(painelBotoesLista);
        BorderPane.setMargin(painelBotoesLista, new Insets(10, 0, 0, 0));

        botaoAdicionar.setOnAction(evento -> {
            try {
                String titulo = campoTituloMusica.getText().trim();
                String artista = campoArtista.getText().trim();
                String nomeAlbum = campoNomeAlbum.getText().trim();

                if (titulo.isEmpty() || artista.isEmpty() || nomeAlbum.isEmpty() || campoAno.getText().trim().isEmpty()) {
                    mostrarAlerta("Erro de Entrada", "Todos os campos são obrigatórios.");
                    return;
                }
                int ano = Integer.parseInt(campoAno.getText().trim());
                catalogoService.adicionarMusica(new Musica(titulo, artista, nomeAlbum, ano));
                limparCamposMusica(campoTituloMusica, campoArtista, campoNomeAlbum, campoAno);
            } catch (NumberFormatException ex) {
                mostrarAlerta("Erro de Formato", "O ano deve ser um número válido (ex: 2023).");
            }
        });

        botaoLimparCampos.setOnAction(evento -> limparCamposMusica(campoTituloMusica, campoArtista, campoNomeAlbum, campoAno));

        botaoExcluir.setOnAction(evento -> {
            Musica musicaSelecionada = visualizadorDeListaMusicas.getSelectionModel().getSelectedItem();
            if (musicaSelecionada != null) {
                if (confirmarExclusao("Excluir Música: " + musicaSelecionada.getTituloMusica(),
                        "Tem certeza que deseja excluir esta música?")) {
                    catalogoService.excluirMusica(musicaSelecionada);
                    limparCamposMusica(campoTituloMusica, campoArtista, campoNomeAlbum, campoAno);
                }
            } else {
                mostrarAlerta("Nenhuma Seleção", "Por favor, selecione uma música para excluir.");
            }
        });
        return painelPrincipalAba;
    }

    private static void limparCamposMusica(TextField campoTitulo, TextField campoArtista, TextField campoNomeAlbum, TextField campoAno) {
        campoTitulo.clear();
        campoArtista.clear();
        campoNomeAlbum.clear();
        campoAno.clear();
        campoTitulo.requestFocus();
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