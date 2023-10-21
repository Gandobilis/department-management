package com.example.demo;

import com.example.demo.windows.Departments;
import com.example.demo.windows.Employees;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    private static Scene scene;
    private static Stage stage;

    @Override
    public void start(Stage window) {
        stage = window;

        window.setTitle("Department Management");

        HBox hBox = new HBox();

        Button showDepartmentsButton = new Button("Departments");
        showDepartmentsButton.setOnAction((e) -> {
            window.setTitle("Departments");
            window.setScene(new Departments().getScene());
        });

        Button showEmployeesButton = new Button("Employees");
        showEmployeesButton.setOnAction((e) -> {
            window.setTitle("Employees");
            window.setScene(new Employees().getScene());
        });

        hBox.getChildren().addAll(
                showDepartmentsButton,
                showEmployeesButton
        );

        hBox.setAlignment(Pos.CENTER);

        StackPane layout = new StackPane();
        layout.getChildren().add(hBox);

        scene = new Scene(layout, 500, 500);
        window.setScene(scene);
        window.show();
    }

    public static void setDefaultScene() {
        stage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
