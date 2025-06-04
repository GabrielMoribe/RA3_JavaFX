package com.example.demo.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import com.example.demo.entidades.Album;
import com.example.demo.services.CatalogoService;

import java.util.Optional;

public class AlbumUI {

    public static BorderPane criarPainelCrudAlbuns(CatalogoService catalogoService) {
        BorderPane painelPrincipalAba = new BorderPane();
        painelPrincipalAba.setPadding(new Insets(10));

        ListView<Album> visualizadorDeListaAlbuns = new ListView<>(catalogoService.getListaDeAlbuns());

        TextField campoTituloAlbum = new TextField();
        campoTituloAlbum.setPromptText("Título do Álbum");
        TextField campoArtistaPrincipal = new TextField();
        campoArtistaPrincipal.setPromptText("Artista Principal");
        TextField campoAnoLancamento = new TextField();
        campoAnoLancamento.setPromptText("Ano de Lançamento (ex: 2023)");
        TextField campoGenero = new TextField();
        campoGenero.setPromptText("Gênero");


        Button botaoAdicionar = new Button("Adicionar Álbum");
        Button botaoExcluir = new Button("Excluir Selecionado");
        Button botaoLimparCampos = new Button("Limpar Campos");

        GridPane painelFormulario = new GridPane();
        painelFormulario.setVgap(8);
        painelFormulario.setHgap(8);
        painelFormulario.add(new Label("Título Álbum:"), 0, 0);
        painelFormulario.add(campoTituloAlbum, 1, 0);
        painelFormulario.add(new Label("Artista Principal:"), 0, 1);
        painelFormulario.add(campoArtistaPrincipal, 1, 1);
        painelFormulario.add(new Label("Ano Lançamento:"), 0, 2);
        painelFormulario.add(campoAnoLancamento, 1, 2);
        painelFormulario.add(new Label("Gênero:"), 0, 3);
        painelFormulario.add(campoGenero, 1, 3);


        HBox painelBotoesFormulario = new HBox(10, botaoAdicionar, botaoLimparCampos);
        painelBotoesFormulario.setAlignment(Pos.CENTER_LEFT);

        VBox painelSuperior = new VBox(15, painelFormulario, painelBotoesFormulario);

        HBox painelBotoesLista = new HBox(10, botaoExcluir);
        painelBotoesLista.setAlignment(Pos.CENTER_RIGHT);

        painelPrincipalAba.setTop(painelSuperior);
        painelPrincipalAba.setCenter(visualizadorDeListaAlbuns);
        painelPrincipalAba.setBottom(painelBotoesLista);
        BorderPane.setMargin(painelBotoesLista, new Insets(10, 0, 0, 0));

        botaoAdicionar.setOnAction(evento -> {
            try {
                String titulo = campoTituloAlbum.getText().trim();
                String artista = campoArtistaPrincipal.getText().trim();
                String genero = campoGenero.getText().trim();

                if (titulo.isEmpty() || artista.isEmpty() || campoAnoLancamento.getText().trim().isEmpty() || genero.isEmpty()) {
                    mostrarAlerta("Erro de Entrada", "Todos os campos são obrigatórios.");
                    return;
                }
                int ano = Integer.parseInt(campoAnoLancamento.getText().trim());
                catalogoService.adicionarAlbum(new Album(titulo, artista, ano, genero));
                limparCamposAlbum(campoTituloAlbum, campoArtistaPrincipal, campoAnoLancamento, campoGenero);
            } catch (NumberFormatException ex) {
                mostrarAlerta("Erro de Formato", "O ano de lançamento deve ser um número válido.");
            }
        });

        botaoLimparCampos.setOnAction(evento -> limparCamposAlbum(campoTituloAlbum, campoArtistaPrincipal, campoAnoLancamento, campoGenero));

        botaoExcluir.setOnAction(evento -> {
            Album albumSelecionado = visualizadorDeListaAlbuns.getSelectionModel().getSelectedItem();
            if (albumSelecionado != null) {
                if (confirmarExclusao("Excluir Álbum: " + albumSelecionado.getTituloAlbum(),
                        "Tem certeza que deseja excluir este álbum?")) {
                    catalogoService.excluirAlbum(albumSelecionado);
                    limparCamposAlbum(campoTituloAlbum, campoArtistaPrincipal, campoAnoLancamento, campoGenero);
                }
            } else {
                mostrarAlerta("Nenhuma Seleção", "Por favor, selecione um álbum para excluir.");
            }
        });
        return painelPrincipalAba;
    }

    private static void limparCamposAlbum(TextField campoTitulo, TextField campoArtista, TextField campoAno, TextField campoGenero) {
        campoTitulo.clear();
        campoArtista.clear();
        campoAno.clear();
        campoGenero.clear();
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
