package com.example.demo;

import com.example.demo.windows.EmployeesWindow;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Department Management App");

        HBox hBox = new HBox();
//        Button button = new Button("Departments");
//        button.setOnAction((e) -> {
//            stage.setScene(new DepartmentsTable().start());
//        });
        Button button1 = new Button("Employees");
        button1.setOnAction((e) -> {
            stage.setScene(new EmployeesWindow().getScene());
            stage.setMaximized(true);
        });

        hBox.getChildren().addAll(
                // button
                button1
        );

        hBox.setAlignment(Pos.CENTER);

        StackPane layout = new StackPane();
        layout.getChildren().add(hBox);

        Scene scene = new Scene(layout, 500, 500);
        stage.setScene(scene);
        stage.show();
    }
}
