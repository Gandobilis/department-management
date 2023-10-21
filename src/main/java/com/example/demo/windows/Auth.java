package com.example.demo.windows;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.stage.Stage;

public class Auth extends Application {
    private final String[] users = { "user1", "user2", "user3" };
    private final String[] passwords = { "password1", "password2", "password3" };

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Stylish Authentication");

        // Create the login form
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label title = new Label("Login");
        title.setStyle("-fx-font-size: 24");
        GridPane.setHalignment(title, HPos.CENTER);
        grid.add(title, 0, 0, 2, 1);

        Label usernameLabel = new Label("Username:");
        grid.add(usernameLabel, 0, 1);

        TextField usernameField = new TextField();
        grid.add(usernameField, 1, 1);

        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 2);

        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 2);

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        grid.add(loginButton, 1, 3);

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (authenticate(username, password)) {
                showSuccessDialog("Welcome, " + username + "!");
            } else {
                showErrorDialog("Login Failed", "Invalid username or password.");
            }
        });

        Scene scene = new Scene(grid, 350, 250);
//        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Authentication logic
    private boolean authenticate(String username, String password) {
        for (int i = 0; i < users.length; i++) {
            if (users[i].equals(username) && passwords[i].equals(password)) {
                return true;
            }
        }
        return false;
    }

    // Show a success dialog
    private void showSuccessDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Show an error dialog
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


