module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires java.sql;

    opens com.example.demo.database to javafx.fxml;
    opens com.example.demo.models to javafx.fxml;
    opens com.example.demo.tables to javafx.fxml;
    opens com.example.demo.windows to javafx.fxml;
    exports com.example.demo.database;
    exports com.example.demo.models;
    exports com.example.demo.tables;
    exports com.example.demo.windows;
}