module com.example.embasa {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.embasa to javafx.fxml;
    exports com.example.embasa;
}