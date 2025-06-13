package com.example.demo.view;

import com.example.demo.entidades.Album;
import com.example.demo.entidades.Musica;
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

public class MusicaUI {

    // Referência para o combobox de álbuns
    private static ComboBox<String> comboAlbunsGlobal;
    private static CatalogoService catalogoServiceGlobal;    public static BorderPane criarPainelCrudMusicas(CatalogoService catalogoService) {
        catalogoServiceGlobal = catalogoService;
        
        BorderPane painelPrincipalAba = new BorderPane();
        painelPrincipalAba.setPadding(new Insets(10));

        ListView<Musica> visualizadorDeListaMusicas = new ListView<>(catalogoService.getListaDeMusicas());        // --- Campos de Texto do Formulário ---
        TextField campoTituloMusica = new TextField();
        campoTituloMusica.setPromptText("Título da Música");
        TextField campoArtista = new TextField();
        campoArtista.setPromptText("Artista");
        
        // ComboBox para álbuns existentes
        ComboBox<String> comboAlbuns = new ComboBox<>();
        comboAlbunsGlobal = comboAlbuns; // Armazenar referência global
        comboAlbuns.setPromptText("Selecione um álbum ou digite um novo");
        comboAlbuns.setEditable(true);
        comboAlbuns.setPrefWidth(200);
        
        // Carregar álbuns do usuário logado
        atualizarComboAlbuns(comboAlbuns, catalogoService);
        
        TextField campoAno = new TextField();
        campoAno.setPromptText("Ano (ex: 2023)");

        // --- Botões ---
        Button botaoAdicionar = new Button("Adicionar Música");
        Button botaoEditar = new Button("Editar Selecionado"); // Botão de Edição Adicionado
        Button botaoExcluir = new Button("Excluir Selecionado");
        Button botaoLimparCampos = new Button("Limpar Campos");

        // --- Layout do Formulário (Topo) ---
        GridPane painelFormulario = new GridPane();
        painelFormulario.setVgap(8);
        painelFormulario.setHgap(8);        painelFormulario.add(new Label("Título:"), 0, 0);
        painelFormulario.add(campoTituloMusica, 1, 0);
        painelFormulario.add(new Label("Artista:"), 0, 1);
        painelFormulario.add(campoArtista, 1, 1);
        painelFormulario.add(new Label("Álbum:"), 0, 2);
        painelFormulario.add(comboAlbuns, 1, 2);
        painelFormulario.add(new Label("Ano:"), 0, 3);
        painelFormulario.add(campoAno, 1, 3);

        HBox painelBotoesFormulario = new HBox(10, botaoAdicionar, botaoLimparCampos);
        painelBotoesFormulario.setAlignment(Pos.CENTER_LEFT);

        VBox painelSuperior = new VBox(15, painelFormulario, painelBotoesFormulario);

        // --- Layout dos Botões de Ação da Lista (Inferior) ---
        HBox painelBotoesLista = new HBox(10, botaoEditar, botaoExcluir); // Botão de Edição Adicionado ao Layout
        painelBotoesLista.setAlignment(Pos.CENTER_RIGHT);        // --- Organização do Painel Principal ---
        painelPrincipalAba.setTop(painelSuperior);
        painelPrincipalAba.setCenter(visualizadorDeListaMusicas);
        painelPrincipalAba.setBottom(painelBotoesLista);
        BorderPane.setMargin(painelBotoesLista, new Insets(10, 0, 0, 0));

        // Adicionar listener para atualizar ComboBox quando o painel for exibido
        painelPrincipalAba.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                // Atualizar lista de álbuns no ComboBox quando o painel for adicionado à cena
                atualizarComboAlbuns(comboAlbuns, catalogoService);
            }
        });// --- Lógica dos Botões ---
        botaoAdicionar.setOnAction(evento -> {
            try {
                String titulo = campoTituloMusica.getText().trim();
                String artista = campoArtista.getText().trim();
                String nomeAlbum = comboAlbuns.getValue();
                
                if (nomeAlbum == null) {
                    nomeAlbum = comboAlbuns.getEditor().getText().trim();
                }                if (titulo.isEmpty() || artista.isEmpty() || nomeAlbum.isEmpty() || campoAno.getText().trim().isEmpty()) {
                    mostrarAlerta("Erro de Entrada", "Todos os campos são obrigatórios.");
                    return;
                }

                int ano = Integer.parseInt(campoAno.getText().trim());
                
                // Validar se o ano é válido
                if (!isAnoValido(ano)) {
                    int anoAtual = java.time.Year.now().getValue();
                    mostrarAlerta("Erro de Formato", 
                        "O ano deve estar entre 1800 e " + (anoAtual + 5) + ".");
                    return;
                }
                
                // Buscar ou criar o álbum
                Album album = catalogoService.buscarAlbumPorNome(nomeAlbum);
                if (album == null) {
                    // Se o álbum não existe, criar um novo com dados básicos
                    catalogoService.adicionarAlbumComDependencia(nomeAlbum, artista, ano, "Indefinido");
                    album = catalogoService.buscarAlbumPorNome(nomeAlbum);
                }
                
                if (album != null) {
                    catalogoService.adicionarMusicaComDependencia(titulo, artista, ano, album);
                    limparCamposMusica(campoTituloMusica, campoArtista, comboAlbuns, campoAno);
                    // Atualizar combo de álbuns após adicionar música
                    atualizarComboAlbuns(comboAlbuns, catalogoService);
                } else {
                    mostrarAlerta("Erro", "Não foi possível criar ou encontrar o álbum.");
                }
            } catch (NumberFormatException ex) {
                mostrarAlerta("Erro de Formato", "O ano deve ser um número válido (ex: 2023).");
            }
        });

        botaoLimparCampos.setOnAction(evento -> limparCamposMusica(campoTituloMusica, campoArtista, comboAlbuns, campoAno));

        // --- Lógica do Botão Editar ---
        botaoEditar.setOnAction(evento -> {
            Musica musicaSelecionada = visualizadorDeListaMusicas.getSelectionModel().getSelectedItem();
            if (musicaSelecionada != null) {
                abrirModalEdicaoMusica(musicaSelecionada, visualizadorDeListaMusicas);
            } else {
                mostrarAlerta("Nenhuma Seleção", "Por favor, selecione uma música para editar.");
            }
        });

        botaoExcluir.setOnAction(evento -> {
            Musica musicaSelecionada = visualizadorDeListaMusicas.getSelectionModel().getSelectedItem();
            if (musicaSelecionada != null) {
                if (confirmarExclusao("Excluir Música: " + musicaSelecionada.getTituloMusica(),
                        "Tem certeza que deseja excluir esta música?")) {
                    catalogoService.excluirMusica(musicaSelecionada);
                }
            } else {
                mostrarAlerta("Nenhuma Seleção", "Por favor, selecione uma música para excluir.");
            }
        });

        return painelPrincipalAba;
    }

    private static void abrirModalEdicaoMusica(Musica musicaParaEditar, ListView<Musica> listView) {
        Stage modalEdicao = new Stage();
        modalEdicao.setTitle("Editar Música");
        modalEdicao.initModality(Modality.APPLICATION_MODAL); // Bloqueia interação com a janela principal
        modalEdicao.setResizable(false);

        // Campos de edição, preenchidos com os dados da música selecionada
        TextField campoTituloEdicao = new TextField(musicaParaEditar.getTituloMusica());
        campoTituloEdicao.setPromptText("Título da Música");
        TextField campoArtistaEdicao = new TextField(musicaParaEditar.getArtista());
        campoArtistaEdicao.setPromptText("Artista");
        TextField campoAlbumEdicao = new TextField(musicaParaEditar.getNomeAlbum());
        campoAlbumEdicao.setPromptText("Álbum");
        TextField campoAnoEdicao = new TextField(String.valueOf(musicaParaEditar.getAno()));
        campoAnoEdicao.setPromptText("Ano");


        // Formulário da modal
        GridPane formularioEdicao = new GridPane();
        formularioEdicao.setVgap(10);
        formularioEdicao.setHgap(10);
        formularioEdicao.setPadding(new Insets(20));

        formularioEdicao.add(new Label("Título da Música:"), 0, 0);
        formularioEdicao.add(campoTituloEdicao, 1, 0);
        formularioEdicao.add(new Label("Artista:"), 0, 1);
        formularioEdicao.add(campoArtistaEdicao, 1, 1);
        formularioEdicao.add(new Label("Álbum:"), 0, 2);
        formularioEdicao.add(campoAlbumEdicao, 1, 2);
        formularioEdicao.add(new Label("Ano:"), 0, 3);
        formularioEdicao.add(campoAnoEdicao, 1, 3);


        // Botões da modal
        Button botaoSalvar = new Button("Salvar");
        Button botaoCancelar = new Button("Cancelar");

        HBox painelBotoes = new HBox(10, botaoSalvar, botaoCancelar);
        painelBotoes.setAlignment(Pos.CENTER_RIGHT);
        painelBotoes.setPadding(new Insets(10, 20, 20, 20));

        // Layout principal da modal
        BorderPane layoutModal = new BorderPane();
        layoutModal.setCenter(formularioEdicao);
        layoutModal.setBottom(painelBotoes);

        // Ações dos botões da modal
        botaoSalvar.setOnAction(evento -> {
            try {
                String novoTitulo = campoTituloEdicao.getText().trim();
                String novoArtista = campoArtistaEdicao.getText().trim();
                String novoAlbum = campoAlbumEdicao.getText().trim();
                String novoAnoStr = campoAnoEdicao.getText().trim();                if (novoTitulo.isEmpty() || novoArtista.isEmpty() || novoAlbum.isEmpty() || novoAnoStr.isEmpty()) {
                    mostrarAlerta("Erro de Entrada", "Todos os campos são obrigatórios.");
                    return;
                }

                int novoAno = Integer.parseInt(novoAnoStr);
                
                // Validar se o ano é válido
                if (!isAnoValido(novoAno)) {
                    int anoAtual = java.time.Year.now().getValue();
                    mostrarAlerta("Erro de Formato", 
                        "O ano deve estar entre 1800 e " + (anoAtual + 5) + ".");
                    return;
                }

                // Atualiza o objeto Musica original que está na lista
                musicaParaEditar.setTituloMusica(novoTitulo);
                musicaParaEditar.setArtista(novoArtista);
                musicaParaEditar.setNomeAlbum(novoAlbum);
                musicaParaEditar.setAno(novoAno);

                // Força a atualização da ListView para refletir a mudança no texto
                listView.refresh();

                mostrarAlerta("Sucesso", "Música editada com sucesso!");
                modalEdicao.close();

            } catch (NumberFormatException ex) {
                mostrarAlerta("Erro de Formato", "O ano deve ser um número válido.");
            } catch (Exception ex) {
                mostrarAlerta("Erro", "Ocorreu um erro ao editar a música: " + ex.getMessage());
            }
        });

        botaoCancelar.setOnAction(evento -> modalEdicao.close());

        // Configurar e exibir a modal
        Scene cenaModal = new Scene(layoutModal, 400, 250);
        modalEdicao.setScene(cenaModal);
        modalEdicao.showAndWait(); // Exibe e espera a modal ser fechada
    }    private static void atualizarComboAlbuns(ComboBox<String> comboAlbuns, CatalogoService catalogoService) {
        // Recarregar dados para garantir que temos os álbuns mais recentes
        catalogoService.recarregarDadosUsuarioLogado();
        
        comboAlbuns.getItems().clear();
        catalogoService.getListaDeAlbuns().forEach(album -> {
            if (!comboAlbuns.getItems().contains(album.getTituloAlbum())) {
                comboAlbuns.getItems().add(album.getTituloAlbum());
            }
        });
    }

    private static void limparCamposMusica(TextField campoTitulo, TextField campoArtista, ComboBox<String> comboAlbum, TextField campoAno) {
        campoTitulo.clear();
        campoArtista.clear();
        comboAlbum.setValue(null);
        comboAlbum.getEditor().clear();
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

    // Atualiza a combobox
    public static void atualizarComboAlbunsExternamente() {
        if (comboAlbunsGlobal != null && catalogoServiceGlobal != null) {
            atualizarComboAlbuns(comboAlbunsGlobal, catalogoServiceGlobal);
        }
    }

    // Método para validar ano de lançamento
    private static boolean isAnoValido(int ano) {
        int anoAtual = java.time.Year.now().getValue();
        // Considera válidos anos entre 1800 e 5 anos no futuro
        return ano >= 1800 && ano <= (anoAtual + 5);
    }
}
