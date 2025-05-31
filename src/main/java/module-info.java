module org.example.projeto_javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires jdk.jfr;
    requires static lombok;

    opens org.example.projeto_javafx to javafx.fxml;
    opens org.example.projeto_javafx.entidades.user to java.base;
    exports org.example.projeto_javafx;
    exports org.example.projeto_javafx.controllers;
    opens org.example.projeto_javafx.controllers to javafx.fxml;
}