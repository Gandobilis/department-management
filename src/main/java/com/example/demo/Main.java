package com.example.demo;

import com.example.demo.windows.Departments;
import com.example.demo.windows.Employees;
import com.example.demo.windows.Table;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage stage;
    private static final String DEFAULT_TITLE = "Department Management";
    private static Scene DEFAULT_SCENE;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle(DEFAULT_TITLE);

        Departments departments = new Departments();
        Employees employees = new Employees();

        HBox hBox = new HBox(
                showButton("Departments", departments, "Departments Table"),
                showButton("Employees", employees, "Employees Table")
        );
        hBox.setSpacing(3);

        hBox.setAlignment(Pos.CENTER);

        StackPane layout = new StackPane(hBox);
        DEFAULT_SCENE = new Scene(layout, 1280, 720);
        stage.setScene(DEFAULT_SCENE);
        stage.show();
    }

    private Button showButton(String buttonText, Table table, String newTitle) {
        Button button = new Button(buttonText);
        button.setOnAction((e) -> {
            stage.setTitle(newTitle);
            table.loadData();
            stage.setScene(table.getScene());
        });
        return button;
    }

    public static void setDefaultScene() {
        stage.setTitle(DEFAULT_TITLE);
        stage.setScene(DEFAULT_SCENE);
    }
}

