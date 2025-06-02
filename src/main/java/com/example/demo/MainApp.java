package com.example.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import service.CatalogoService;
import ui.AlbumUI;       // Importa a UI de Album
import ui.MusicaUI;       // Importa a UI de Musica

public class MainApp extends Application {

    private CatalogoService catalogoService = new CatalogoService();

    @Override
    public void start(Stage palcoPrincipal) {
        palcoPrincipal.setTitle("Gerenciador de Músicas e Álbuns");

        TabPane painelDeAbas = new TabPane();

        Tab abaMusicas = new Tab("Músicas");
        abaMusicas.setClosable(false);
        abaMusicas.setContent(MusicaUI.criarPainelCrudMusicas(catalogoService));
        painelDeAbas.getTabs().add(abaMusicas);

        Tab abaAlbuns = new Tab("Álbuns");
        abaAlbuns.setClosable(false);
        abaAlbuns.setContent(AlbumUI.criarPainelCrudAlbuns(catalogoService));
        painelDeAbas.getTabs().add(abaAlbuns);

        Scene cenaPrincipal = new Scene(painelDeAbas, 700, 550); // Ajuste de tamanho se necessário
        palcoPrincipal.setScene(cenaPrincipal);
        palcoPrincipal.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}