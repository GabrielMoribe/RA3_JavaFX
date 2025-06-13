package com.example.demo.view;
import com.example.demo.services.AuthService;
import com.example.demo.entidades.Album;
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

public class AlbumUI {

    public static BorderPane criarPainelCrudAlbuns(CatalogoService catalogoService) {
        BorderPane painelPrincipalAba = new BorderPane();
        painelPrincipalAba.setPadding(new Insets(10));

        ListView<Album> visualizadorDeListaAlbuns = new ListView<>(catalogoService.getListaDeAlbuns());

        // --- Campos de Texto do Formulário ---
        TextField campoTituloAlbum = new TextField();
        campoTituloAlbum.setPromptText("Título do Álbum");
        TextField campoArtistaPrincipal = new TextField();
        campoArtistaPrincipal.setPromptText("Artista Principal");
        TextField campoAnoLancamento = new TextField();
        campoAnoLancamento.setPromptText("Ano de Lançamento (ex: 2023)");
        TextField campoGenero = new TextField();
        campoGenero.setPromptText("Gênero");

        // --- Botões ---
        Button botaoAdicionar = new Button("Adicionar Álbum");
        Button botaoEditar = new Button("Editar Selecionado"); // Botão de Edição Adicionado
        Button botaoExcluir = new Button("Excluir Selecionado");
        Button botaoLimparCampos = new Button("Limpar Campos");

        // --- Layout do Formulário (Topo) ---
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

        // --- Layout dos Botões de Ação da Lista (Inferior) ---
        HBox painelBotoesLista = new HBox(10, botaoEditar, botaoExcluir); // Botão de Edição Adicionado ao Layout
        painelBotoesLista.setAlignment(Pos.CENTER_RIGHT);

        // --- Organização do Painel Principal ---
        painelPrincipalAba.setTop(painelSuperior);
        painelPrincipalAba.setCenter(visualizadorDeListaAlbuns);
        painelPrincipalAba.setBottom(painelBotoesLista);
        BorderPane.setMargin(painelBotoesLista, new Insets(10, 0, 0, 0));

        // --- Lógica dos Botões ---
        botaoAdicionar.setOnAction(evento -> {
            try {
                String titulo = campoTituloAlbum.getText().trim();
                String artista = campoArtistaPrincipal.getText().trim();
                String genero = campoGenero.getText().trim();

                if (titulo.isEmpty() || artista.isEmpty() || campoAnoLancamento.getText().trim().isEmpty() || genero.isEmpty()) {
                    mostrarAlerta("Erro de Entrada", "Todos os campos são obrigatórios.");
                    return;
                }                int ano = Integer.parseInt(campoAnoLancamento.getText().trim());
                catalogoService.adicionarAlbumComDependencia(titulo, artista, ano, genero);
                limparCamposAlbum(campoTituloAlbum, campoArtistaPrincipal, campoAnoLancamento, campoGenero);
            } catch (NumberFormatException ex) {
                mostrarAlerta("Erro de Formato", "O ano de lançamento deve ser um número válido.");
            }
        });

        botaoLimparCampos.setOnAction(evento -> limparCamposAlbum(campoTituloAlbum, campoArtistaPrincipal, campoAnoLancamento, campoGenero));

        // --- Lógica do Botão Editar ---
        botaoEditar.setOnAction(evento -> {
            Album albumSelecionado = visualizadorDeListaAlbuns.getSelectionModel().getSelectedItem();
            if (albumSelecionado != null) {
                abrirModalEdicaoAlbum(albumSelecionado, catalogoService, visualizadorDeListaAlbuns);
            } else {
                mostrarAlerta("Nenhuma Seleção", "Por favor, selecione um álbum para editar.");
            }
        });

        botaoExcluir.setOnAction(evento -> {
            Album albumSelecionado = visualizadorDeListaAlbuns.getSelectionModel().getSelectedItem();
            if (albumSelecionado != null) {
                if (confirmarExclusao("Excluir Álbum: " + albumSelecionado.getTituloAlbum(),
                        "Tem certeza que deseja excluir este álbum?")) {
                    catalogoService.excluirAlbum(albumSelecionado);
                }
            } else {
                mostrarAlerta("Nenhuma Seleção", "Por favor, selecione um álbum para excluir.");
            }
        });
        return painelPrincipalAba;
    }

    // Modificar a modal de edição para incluir sincronização
    private static void abrirModalEdicaoAlbum(Album albumParaEditar, CatalogoService catalogoService, ListView<Album> visualizadorDeListaAlbuns) {
        Stage janelaModal = new Stage();
        janelaModal.setTitle("Editar Álbum");
        janelaModal.initModality(Modality.APPLICATION_MODAL);

        // Campos de edição
        TextField campoTituloEdicao = new TextField(albumParaEditar.getTituloAlbum());
        campoTituloEdicao.setPromptText("Título do Álbum");
        TextField campoArtistaEdicao = new TextField(albumParaEditar.getArtistaPrincipal());
        campoArtistaEdicao.setPromptText("Artista Principal");
        TextField campoAnoEdicao = new TextField(String.valueOf(albumParaEditar.getAnoLancamento()));
        campoAnoEdicao.setPromptText("Ano de Lançamento");
        TextField campoGeneroEdicao = new TextField(albumParaEditar.getGenero());
        campoGeneroEdicao.setPromptText("Gênero");

        Button botaoSalvar = new Button("Salvar");
        Button botaoCancelar = new Button("Cancelar");

        // Ação do botão salvar com sincronização
        botaoSalvar.setOnAction(evento -> {
            try {
                String tituloAntigo = albumParaEditar.getTituloAlbum(); // Guardar título antigo
                String novoTitulo = campoTituloEdicao.getText().trim();
                String novoArtista = campoArtistaEdicao.getText().trim();
                String novoGenero = campoGeneroEdicao.getText().trim();

                if (novoTitulo.isEmpty() || novoArtista.isEmpty() || campoAnoEdicao.getText().trim().isEmpty() || novoGenero.isEmpty()) {
                    mostrarAlerta("Erro de Entrada", "Todos os campos são obrigatórios.");
                    return;
                }

                int novoAno = Integer.parseInt(campoAnoEdicao.getText().trim());

                // SINCRONIZAÇÃO: Se o título mudou, atualizar as músicas
                if (!tituloAntigo.equals(novoTitulo)) {
                    catalogoService.atualizarNomeAlbumEmMusicas(
                            tituloAntigo,
                            novoTitulo,
                            AuthService.getUsuarioLogado().getEmail()
                    );
                }

                // Atualizar o álbum
                albumParaEditar.setTituloAlbum(novoTitulo);
                albumParaEditar.setArtistaPrincipal(novoArtista);
                albumParaEditar.setAnoLancamento(novoAno);
                albumParaEditar.setGenero(novoGenero);

                // Salvar alterações
                catalogoService.atualizarAlbum(albumParaEditar);
                visualizadorDeListaAlbuns.refresh();

                mostrarAlerta("Sucesso", "Álbum editado com sucesso!");
                janelaModal.close();

            } catch (NumberFormatException ex) {
                mostrarAlerta("Erro de Formato", "O ano deve ser um número válido (ex: 2023).");
            } catch (Exception ex) {
                mostrarAlerta("Erro", "Erro ao editar álbum: " + ex.getMessage());
            }
        });

        // Ação do botão cancelar
        botaoCancelar.setOnAction(evento -> janelaModal.close());

        // Layout da modal
        GridPane layoutModal = new GridPane();
        layoutModal.setHgap(10);
        layoutModal.setVgap(10);
        layoutModal.setPadding(new Insets(20));

        layoutModal.add(new Label("Título:"), 0, 0);
        layoutModal.add(campoTituloEdicao, 1, 0);
        layoutModal.add(new Label("Artista:"), 0, 1);
        layoutModal.add(campoArtistaEdicao, 1, 1);
        layoutModal.add(new Label("Ano:"), 0, 2);
        layoutModal.add(campoAnoEdicao, 1, 2);
        layoutModal.add(new Label("Gênero:"), 0, 3);
        layoutModal.add(campoGeneroEdicao, 1, 3);

        HBox painelBotoes = new HBox(10, botaoSalvar, botaoCancelar);
        painelBotoes.setAlignment(Pos.CENTER);
        layoutModal.add(painelBotoes, 0, 4, 2, 1);

        Scene cenaModal = new Scene(layoutModal, 400, 250);
        janelaModal.setScene(cenaModal);
        janelaModal.showAndWait();
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
