module com.example.hidrometro {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.hidrometro to javafx.fxml;
    exports com.example.hidrometro;
}