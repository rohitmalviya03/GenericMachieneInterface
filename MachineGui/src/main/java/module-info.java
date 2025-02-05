module org.example.machinegui {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.machinegui to javafx.fxml;
    exports org.example.machinegui;
}