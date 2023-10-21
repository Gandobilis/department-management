package com.example.demo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AuthController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    public void loginButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (authenticate(username, password)) {
            showSuccessDialog("Welcome, " + username + "!");
        } else {
            showErrorDialog("Login Failed", "Invalid username or password.");
        }
    }

    // Authentication logic (same as in your original code)
    private boolean authenticate(String username, String password) {
        // Your authentication logic here
        for (int i = 0; i < users.length; i++) {
            if (users[i].equals(username) && passwords[i].equals(password)) {
                return true;
            }
        }
        return false;
    }

    // Show a success dialog (same as in your original code)
    private void showSuccessDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Show an error dialog (same as in your original code)
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // You can define the users and passwords arrays here
    private final String[] users = {"user1", "user2", "user3"};
    private final String[] passwords = {"password1", "password2", "password3"};
}
