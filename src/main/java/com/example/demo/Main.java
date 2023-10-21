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
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Department Management");

        HBox hBox = new HBox();

        Button showDepartmentsButton = new Button("Departments");
        showDepartmentsButton.setOnAction((e) -> {
            stage.setTitle("Departments");
            stage.setScene(new Departments().getScene());
        });

        Button showEmployeesButton = new Button("Employees");
        showEmployeesButton.setOnAction((e) -> {
            stage.setTitle("Employees");
            stage.setScene(new Employees().getScene());
        });

        hBox.getChildren().addAll(
                showDepartmentsButton,
                showEmployeesButton
        );

        hBox.setAlignment(Pos.CENTER);

        StackPane layout = new StackPane();
        layout.getChildren().add(hBox);

        Scene scene = new Scene(layout, 500, 500);
        stage.setScene(scene);
        stage.show();
    }
}
