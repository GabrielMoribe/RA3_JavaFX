module com.example.demo {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive javafx.base;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires static lombok;

    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
    exports com.example.demo.entidades;
    opens com.example.demo.entidades to javafx.fxml, java.base;
    exports com.example.demo.view;
    opens com.example.demo.view to javafx.fxml;
    exports com.example.demo.entidades.arquivo;
    opens com.example.demo.entidades.arquivo to java.base;
    exports com.example.demo.services;
    opens com.example.demo.services to javafx.fxml;
}