package com.example.demo.tables;

import com.example.demo.database.Connection;
import com.example.demo.models.Department;
import com.example.demo.models.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TEmployees implements Table<Employee> {
    private static TEmployees instance;

    public static TEmployees getInstance() {
        if (instance == null) {
            instance = new TEmployees();
        }
        return instance;
    }

    private TEmployees() {
    }

    @Override
    public Employee findById(Integer id) {
        Employee employee = null;

        try (java.sql.Connection connection = Connection.getConnection()) {
            String query = "SELECT " +
                    "e.id, " +
                    "e.first_name, " +
                    "e.last_name, " +
                    "d.name " +
                    "FROM employees e " +
                    "JOIN departments d ON e.department_id = d.id " +
                    "WHERE e.id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Integer employeeId = resultSet.getInt("id");
                        String firstName = resultSet.getString("first_name");
                        String lastName = resultSet.getString("last_name");
                        String departmentName = resultSet.getString("name");
                        Department department = new Department(departmentName);
                        employee = new Employee(employeeId, firstName, lastName, department);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return employee;
    }

    @Override
    public ObservableList<Employee> findAll() {
        ObservableList<Employee> employees = FXCollections.observableArrayList();

        try (java.sql.Connection connection = Connection.getConnection()) {
            String query = "SELECT " +
                    "e.employee_id, " +
                    "e.first_name, " +
                    "e.last_name, " +
                    "d.department_name " +
                    "FROM employees e join departments d " +
                    "on e.department_id = d.department_id";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Integer id = resultSet.getInt("employee_id");
                        String firstName = resultSet.getString("first_name");
                        String lastName = resultSet.getString("last_name");
                        String name = resultSet.getString("department_name");
                        employees.add(new Employee(id, firstName, lastName, new Department(name)));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return employees;
    }

    @Override
    public void create(Employee employee) {
        try (java.sql.Connection connection = Connection.getConnection()) {
            String query = "INSERT INTO employees (first_name, " +
                    "last_name, " +
                    "department_id) " +
                    "VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, employee.getFirstName());
                preparedStatement.setString(2, employee.getLastName());
                preparedStatement.setInt(3, employee.getDepartment().getId());
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Employee added successfully.");
                } else {
                    System.out.println("Failed to add the employee.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Employee employee) {
        try (java.sql.Connection connection = Connection.getConnection()) {
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
                } else {
                    System.out.println("No employee found with the specified ID. No update performed.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Integer id) {
        try (java.sql.Connection connection = Connection.getConnection()) {
            String query = "DELETE FROM employees " +
                    "WHERE employee_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, id);
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Employee with ID " + id + " deleted successfully.");
                } else {
                    System.out.println("No employee found with ID " + id + ". No deletion performed.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
