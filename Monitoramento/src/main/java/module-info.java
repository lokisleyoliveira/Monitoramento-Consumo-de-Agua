module com.example.monitoramento {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.monitoramento to javafx.fxml;
    exports com.example.monitoramento;
}