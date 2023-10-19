package com.example.demo.tables;

import com.example.demo.database.DatabaseConnection;
import com.example.demo.models.Department;
import com.example.demo.models.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Employees {
    public static void create(Employee employee, Integer departmentId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO employees (first_name, " +
                    "last_name, " +
                    "department_id) " +
                    "VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, employee.getFirstName());
                preparedStatement.setString(2, employee.getLastName());
                preparedStatement.setInt(3, departmentId);
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Employee added successfully.");
                    read();
                } else {
                    System.out.println("Failed to add the employee.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Employee> read() {
        ObservableList<Employee> employees = FXCollections.observableArrayList();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT " +
                    "e.first_name, " +
                    "e.last_name, " +
                    "d.name " +
                    "FROM employees e join departments d " +
                    "on e.department_id = d.id";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Integer id = resultSet.getInt("id");
                        String firstName = resultSet.getString("first_name");
                        String lastName = resultSet.getString("last_name");
                        String name = resultSet.getString("name");
                        employees.add(new Employee(id, firstName, lastName, new Department(name)));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employees;
    }

    public static void update(Employee employee) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "UPDATE employees " +
                    "SET first_name = ?, " +
                    "last_name = ? " +
                    "WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, employee.getFirstName());
                preparedStatement.setString(2, employee.getLastName());
                preparedStatement.setInt(3, employee.getId());
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Employee information updated successfully.");
                    read();
                } else {
                    System.out.println("No employee found with the specified ID. No update performed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(Integer id) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM employees " +
                    "WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, id);
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Employee with ID " + id + " deleted successfully.");
                    read();
                } else {
                    System.out.println("No employee found with ID " + id + ". No deletion performed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void loadDepartment() {

    }
}
