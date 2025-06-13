package com.example.demo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import com.example.demo.services.AuthService;
import com.example.demo.services.CatalogoService;
import com.example.demo.view.AdminUI;
import com.example.demo.view.AlbumUI;
import com.example.demo.view.LoginUI;
import com.example.demo.view.MusicaUI;
import com.example.demo.view.PerfilUI;

public class MainApp extends Application {

    private CatalogoService catalogoService = new CatalogoService();

    @Override
    public void start(Stage palcoPrincipal) {
        // Mostrar tela de login primeiro (Inicio da aplicacao)
        if (!LoginUI.mostrarTelaLogin(palcoPrincipal)) {
            return;
        }


        if (AuthService.isAdmin()) {
            mostrarTelaAdmin(palcoPrincipal);
        } else {
            mostrarTelaPrincipal(palcoPrincipal);
        }
    }

    private void mostrarTelaAdmin(Stage palcoPrincipal) {
        palcoPrincipal.setTitle("Sistema de Catálogo Musical - Painel Administrativo");

        BorderPane layoutPrincipal = new BorderPane();
        BorderPane painelAdmin = AdminUI.criarPainelAdmin(catalogoService);
        
        // Botao logout (Admin)
        Button botaoLogout = new Button("Sair");
        botaoLogout.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-padding: 8px 20px;");
        botaoLogout.setOnAction(e -> {
            AuthService.logout();
            palcoPrincipal.close();
            // Reiniciar o prog
            Platform.runLater(() -> {
                try {
                    new MainApp().start(new Stage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        });
        
        // Botao logout
        HBox painelLogout = new HBox();
        painelLogout.setAlignment(Pos.CENTER_RIGHT);
        painelLogout.setPadding(new Insets(10));
        painelLogout.getChildren().add(botaoLogout);
        
        layoutPrincipal.setTop(painelLogout);
        layoutPrincipal.setCenter(painelAdmin);

        Scene cenaAdmin = new Scene(layoutPrincipal, 900, 700);
        palcoPrincipal.setScene(cenaAdmin);
        palcoPrincipal.show();
    }

    private void mostrarTelaPrincipal(Stage palcoPrincipal) {
        palcoPrincipal.setTitle("Catálogo Musical - " + AuthService.getUsuarioLogado().getNome());

        // Filtrar dados para o usuário logado
        catalogoService.filtrarDadosUsuarioLogado(AuthService.getUsuarioLogado().getEmail());

        BorderPane layoutPrincipal = new BorderPane();
        HBox barraUsuario = new HBox(10);
        barraUsuario.setPadding(new Insets(10));
        barraUsuario.setStyle("-fx-background-color: #f0f0f0;");
        
        Label labelUsuario = new Label("Usuário: " + AuthService.getUsuarioLogado().getNome());
        labelUsuario.setStyle("-fx-font-weight: bold;");
        
        Button botaoLogout = new Button("Sair");
        botaoLogout.setOnAction(e -> {
            AuthService.logout();
            palcoPrincipal.close();
            // Reiniciar a aplicação
            Platform.runLater(() -> {
                try {
                    new MainApp().start(new Stage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        });
        
        barraUsuario.getChildren().addAll(labelUsuario, new Region(), botaoLogout);
        ((Region) barraUsuario.getChildren().get(1)).setPrefWidth(Region.USE_COMPUTED_SIZE);
        HBox.setHgrow(barraUsuario.getChildren().get(1), Priority.ALWAYS);

        TabPane abas = new TabPane();

        Tab abaMusicas = new Tab("Minhas Músicas");
        abaMusicas.setClosable(false);
        abaMusicas.setContent(MusicaUI.criarPainelCrudMusicas(catalogoService));
        abas.getTabs().add(abaMusicas);

        Tab abaAlbuns = new Tab("Meus Álbuns");
        abaAlbuns.setClosable(false);
        abaAlbuns.setContent(AlbumUI.criarPainelCrudAlbuns(catalogoService));
        abas.getTabs().add(abaAlbuns);

        Tab abaPerfil = new Tab("Meu Perfil");
        abaPerfil.setClosable(false);
        PerfilUI perfilUI = new PerfilUI(catalogoService, palcoPrincipal);
        abaPerfil.setContent(perfilUI.criarPainelPerfil());
        abas.getTabs().add(abaPerfil);

        // Adicionar listener para atualizar dados quando a aba Músicas for selecionada
        abas.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab == abaMusicas) {
                // Recarregar dados do usuário logado ao entrar na aba de músicas
                catalogoService.recarregarDadosUsuarioLogado();
                // Atualizar combobox de álbuns
                MusicaUI.atualizarComboAlbunsExternamente();
            } else if (newTab == abaAlbuns) {
                // Recarregar dados do usuário logado ao entrar na aba de álbuns
                catalogoService.recarregarDadosUsuarioLogado();
            } else if (newTab == abaPerfil) {
                // Atualizar painel de perfil
                abaPerfil.setContent(perfilUI.criarPainelPerfil());
            }
        });

        layoutPrincipal.setTop(barraUsuario);
        layoutPrincipal.setCenter(abas);

        Scene cenaPrincipal = new Scene(layoutPrincipal, 800, 600);
        palcoPrincipal.setScene(cenaPrincipal);
        palcoPrincipal.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}