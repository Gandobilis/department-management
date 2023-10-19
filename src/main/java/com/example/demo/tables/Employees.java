package com.example.demo.tables;

import com.example.demo.database.DatabaseConnection;
import com.example.demo.models.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Employees {
    private static List<Employee> data;

    public static void create(Employee employee, Integer departmentId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO employees (first_name, last_name, department_id) VALUES (?, ?, ?)";
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
            System.out.println("Failed to add the employee.");
        }
    }

    public static List<Employee> read() {
        List<Employee> employees = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT id, first_name, last_name FROM employees";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Integer id = resultSet.getInt("id");
                        String firstName = resultSet.getString("first_name");
                        String lastName = resultSet.getString("last_name");
                        employees.add(new Employee(id, firstName, lastName));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to read employees employees.");
        }

        return employees;
    }

    public static void update(Employee employee) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "UPDATE employees SET first_name = ?, last_name = ? WHERE id = ?";
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
            System.out.println("Failed to update employee with id " + employee.getId());
        }
    }

    public static void delete(Integer id) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM employees WHERE id = ?";
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
            System.out.println("Failed to delete employee with id " + id);
        }
    }

    public static List<Employee> getData() {
        if (data == null) {
            data = read();
        }
        return data;
    }
}
