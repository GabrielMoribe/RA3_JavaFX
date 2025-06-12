package com.example.demo.view;

import com.example.demo.MainApp;
import com.example.demo.entidades.User;
import com.example.demo.services.AuthService;
import com.example.demo.services.CatalogoService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PerfilUI {
    private CatalogoService catalogoService;
    private Stage primaryStage;
    
    // Referências para atualizar a interface
    private Label infoNome;
    private Label infoEmail;
    private Label infoTelefone;
    private Label labelMusicas;
    private Label labelAlbuns;
    
    public PerfilUI(CatalogoService catalogoService, Stage primaryStage) {
        this.catalogoService = catalogoService;
        this.primaryStage = primaryStage;
    }

    public VBox criarPainelPerfil() {
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        // Título
        Label titulo = new Label("Meu Perfil");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");        // Informações do usuário
        User usuarioLogado = AuthService.getUsuarioLogado();
        
        infoNome = new Label("Nome: " + usuarioLogado.getNome());
        infoNome.setStyle("-fx-font-size: 16px;");
        
        infoEmail = new Label("Email: " + usuarioLogado.getEmail());
        infoEmail.setStyle("-fx-font-size: 16px;");
        
        infoTelefone = new Label("Telefone: " + usuarioLogado.getTelefone());
        infoTelefone.setStyle("-fx-font-size: 16px;");

        // Botões de ação
        HBox botoesAcao = new HBox(10);
        botoesAcao.setAlignment(Pos.CENTER);        Button btnEditar = new Button("Editar Perfil");
        btnEditar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10px 20px;");
        btnEditar.setOnAction(e -> abrirTelaEdicaoCompleta());

        Button btnExcluirConta = new Button("Excluir Conta");
        btnExcluirConta.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-padding: 10px 20px;");
        btnExcluirConta.setOnAction(e -> confirmarExclusaoConta());

        botoesAcao.getChildren().addAll(btnEditar, btnExcluirConta);        // Estatísticas do usuário
        VBox estatisticas = new VBox(10);
        estatisticas.setAlignment(Pos.CENTER);
        estatisticas.setStyle("-fx-padding: 15px;");
        
        Label tituloEstatisticas = new Label("Estatísticas da conta");
        tituloEstatisticas.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
          int totalMusicas = catalogoService.getListaDeMusicas().size();
        int totalAlbuns = catalogoService.getListaDeAlbuns().size();
        
        labelMusicas = new Label("Total de Músicas: " + totalMusicas);
        labelAlbuns = new Label("Total de Álbuns: " + totalAlbuns);
        
        estatisticas.getChildren().addAll(tituloEstatisticas, labelMusicas, labelAlbuns);
        vbox.getChildren().addAll(titulo, infoNome, infoEmail, infoTelefone, estatisticas, botoesAcao);
          return vbox;
    }

    // Método para atualizar a interface após edição
    private void atualizarInterface() {
        User usuarioLogado = AuthService.getUsuarioLogado();
        
        // Atualizar informações do usuário
        infoNome.setText("Nome: " + usuarioLogado.getNome());
        infoEmail.setText("Email: " + usuarioLogado.getEmail());
        infoTelefone.setText("Telefone: " + usuarioLogado.getTelefone());
        
        // Atualizar estatísticas
        int totalMusicas = catalogoService.getListaDeMusicas().size();
        int totalAlbuns = catalogoService.getListaDeAlbuns().size();
        labelMusicas.setText("Total de Músicas: " + totalMusicas);
        labelAlbuns.setText("Total de Álbuns: " + totalAlbuns);
    }

    private void abrirTelaEdicaoCompleta() {
        Stage editarStage = new Stage();
        editarStage.initModality(Modality.APPLICATION_MODAL);
        editarStage.initOwner(primaryStage);
        editarStage.setTitle("Editar Perfil");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));

        User usuarioLogado = AuthService.getUsuarioLogado();

        // Campos de edição
        Label lblNome = new Label("Nome:");
        TextField txtNome = new TextField(usuarioLogado.getNome());
        
        Label lblEmail = new Label("Email:");
        TextField txtEmail = new TextField(usuarioLogado.getEmail());
          Label lblTelefone = new Label("Telefone:");
        TextField txtTelefone = new TextField(usuarioLogado.getTelefone());
        
        // Aplicar formatação automática do telefone
        UserUI.configurarFormatacaoTelefone(txtTelefone);

        Label lblSenha = new Label("Nova Senha (deixe vazio para manter atual):");
        PasswordField txtSenha = new PasswordField();
        txtSenha.setPromptText("Digite a nova senha ou deixe vazio");

        // Botões
        Button btnSalvar = new Button("Salvar");
        btnSalvar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 8px 20px;");
        
        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-padding: 8px 20px;");

        // Layout
        grid.add(lblNome, 0, 0);
        grid.add(txtNome, 1, 0);
        grid.add(lblEmail, 0, 1);
        grid.add(txtEmail, 1, 1);
        grid.add(lblTelefone, 0, 2);
        grid.add(txtTelefone, 1, 2);
        grid.add(lblSenha, 0, 3);
        grid.add(txtSenha, 1, 3);
        
        HBox botoes = new HBox(10);
        botoes.setAlignment(Pos.CENTER);
        botoes.getChildren().addAll(btnSalvar, btnCancelar);
        grid.add(botoes, 0, 4, 2, 1);        btnSalvar.setOnAction(e -> {
            try {
                String novoNome = txtNome.getText().trim();
                String novoEmail = txtEmail.getText().trim();
                String novoTelefone = txtTelefone.getText().trim();
                String novaSenha = txtSenha.getText().trim();
                  // Validações básicas
                if (novoNome.isEmpty()) {
                    mostrarAlerta("Erro", "O nome não pode estar vazio!");
                    return;
                }
                
                if (novoEmail.isEmpty() || !UserUI.isEmailValido(novoEmail)) {
                    mostrarAlerta("Erro", "Digite um email válido!");
                    return;
                }
                
                if (novoTelefone.isEmpty() || !UserUI.isTelefoneValido(novoTelefone)) {
                    mostrarAlerta("Erro", "Digite um telefone válido!");
                    return;
                }

                // Guardar email original antes de modificar
                String emailOriginal = usuarioLogado.getEmail();
                
                // Atualizar dados do usuário
                usuarioLogado.setNome(novoNome);
                usuarioLogado.setEmail(novoEmail);
                usuarioLogado.setTelefone(novoTelefone);
                
                // Se foi digitada uma nova senha, atualizar
                if (!novaSenha.isEmpty()) {
                    if (novaSenha.length() < 6) {
                        mostrarAlerta("Erro", "A senha deve ter pelo menos 6 caracteres!");
                        return;
                    }
                    usuarioLogado.setSenha(AuthService.criptografarSenha(novaSenha));
                }
                  
                // Salvar alterações passando o email original
                catalogoService.atualizarUsuario(usuarioLogado, emailOriginal);
                
                // Atualizar a interface
                atualizarInterface();
                
                mostrarAlerta("Sucesso", "Perfil atualizado com sucesso!");
                editarStage.close();
                
            } catch (Exception ex) {
                mostrarAlerta("Erro", "Erro ao salvar: " + ex.getMessage());
            }
        });

        btnCancelar.setOnAction(e -> editarStage.close());

        Scene scene = new Scene(grid, 400, 300);
        editarStage.setScene(scene);
        editarStage.showAndWait();
    }

    private void confirmarExclusaoConta() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Exclusão");
        alert.setHeaderText("Excluir Conta");
        alert.setContentText("Tem certeza que deseja excluir sua conta? " +
                           "Esta ação não pode ser desfeita e todos os seus dados " +
                           "(músicas e álbuns) serão perdidos permanentemente.");

        ButtonType buttonTypeSim = new ButtonType("Sim, excluir");
        ButtonType buttonTypeNao = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeSim, buttonTypeNao);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeSim) {
                excluirContaUsuario();
            }
        });
    }

    private void excluirContaUsuario() {
        User usuarioLogado = AuthService.getUsuarioLogado();
        String emailUsuario = usuarioLogado.getEmail();

        // Excluir todas as músicas do usuário
        catalogoService.excluirTodasMusicasDoUsuario(emailUsuario);
        
        // Excluir todos os álbuns do usuário
        catalogoService.excluirTodosAlbunsDoUsuario(emailUsuario);
        
        // Excluir o usuário
        catalogoService.excluirUsuario(usuarioLogado);

        // Fazer logout
        AuthService.logout();

        // Mostrar mensagem e fechar aplicação
        mostrarAlerta("Conta Excluída", "Sua conta foi excluída com sucesso!");
        
        // Voltar para tela de login
        primaryStage.close();
        
        try {
            new MainApp().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
