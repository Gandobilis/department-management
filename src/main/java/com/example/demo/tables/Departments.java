package com.example.demo.tables;

import com.example.demo.database.DatabaseConnection;
import com.example.demo.models.Department;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Departments {
    private static ObservableList<Department> data;

    public static void create(Department department, Integer departmentId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO departments (name) VALUES (?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, department.getName());
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Department added successfully.");
                    read();
                } else {
                    System.out.println("Failed to add the department.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static ObservableList<Department> read() {
        ObservableList<Department> departments = FXCollections.observableArrayList();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT id, name FROM departments";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Integer id = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        departments.add(new Department(id, name));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to read departments departments.");
        }

        return departments;
    }

    public static void update(Department department) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "UPDATE departments SET name = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, department.getName());
                preparedStatement.setInt(2, department.getId());
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Department information updated successfully.");
                    read();
                } else {
                    System.out.println("No department found with the specified ID. No update performed.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to update department with id " + department.getId());
        }
    }

    public static void delete(Integer id) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM departments WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, id);
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Department with ID " + id + " deleted successfully.");
                    read();
                } else {
                    System.out.println("No department found with ID " + id + ". No deletion performed.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to delete department with id " + id);
        }
    }
}