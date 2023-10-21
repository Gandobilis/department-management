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

        HBox hBox = new HBox(
                showButton("Departments", new Departments().getScene(), "Departments Table"),
                showButton("Employees", new Employees().getScene(), "Employees Table")
        );
        hBox.setSpacing(3);

        hBox.setAlignment(Pos.CENTER);

        StackPane layout = new StackPane(hBox);
        DEFAULT_SCENE = new Scene(layout, 1280, 720);
        stage.setScene(DEFAULT_SCENE);
        stage.show();
    }

    private Button showButton(String buttonText, Scene newScene, String newTitle) {
        Button button = new Button(buttonText);
        button.setOnAction((e) -> {
            stage.setTitle(newTitle);
            stage.setScene(newScene);
        });
        return button;
    }

    public static void setDefaultScene() {
        stage.setTitle(DEFAULT_TITLE);
        stage.setScene(DEFAULT_SCENE);
    }
}

