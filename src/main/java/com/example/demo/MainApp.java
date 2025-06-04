package com.example.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import com.example.demo.services.CatalogoService;
import com.example.demo.view.AlbumUI;
import com.example.demo.view.MusicaUI;
import com.example.demo.view.UserUI;

public class MainApp extends Application {

    private CatalogoService catalogoService = new CatalogoService();

    @Override
    public void start(Stage palcoPrincipal) {
        palcoPrincipal.setTitle("Gerenciador de Músicas, Álbuns e Usuários");

        TabPane painelDeAbas = new TabPane();

        Tab abaUsuarios = new Tab("Usuários");
        abaUsuarios.setClosable(false);
        abaUsuarios.setContent(UserUI.criarPainelCrudUsuarios(catalogoService));
        painelDeAbas.getTabs().add(abaUsuarios);

        Tab abaMusicas = new Tab("Músicas");
        abaMusicas.setClosable(false);
        abaMusicas.setContent(MusicaUI.criarPainelCrudMusicas(catalogoService));
        painelDeAbas.getTabs().add(abaMusicas);

        Tab abaAlbuns = new Tab("Álbuns");
        abaAlbuns.setClosable(false);
        abaAlbuns.setContent(AlbumUI.criarPainelCrudAlbuns(catalogoService));
        painelDeAbas.getTabs().add(abaAlbuns);

        Scene cenaPrincipal = new Scene(painelDeAbas, 700, 550);
        palcoPrincipal.setScene(cenaPrincipal);
        palcoPrincipal.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}