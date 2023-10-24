package com.example.demo.tables;

import com.example.demo.database.Connection;
import com.example.demo.models.Department;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TDepartments implements Table<Department> {
    private static TDepartments instance;

    public static TDepartments getInstance() {
        if (instance == null) {
            instance = new TDepartments();
        }
        return instance;
    }

    private TDepartments() {
    }

    @Override
    public Department findById(Integer id) {
        Department department = null;

        try (java.sql.Connection connection = Connection.getConnection()) {
            String query = "SELECT id, name FROM departments WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Integer departmentId = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        department = new Department(departmentId, name);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return department;
    }

    @Override
    public ObservableList<Department> findAll() {
        ObservableList<Department> departments = FXCollections.observableArrayList();

        try (java.sql.Connection connection = Connection.getConnection()) {
            String query = "SELECT d.department_id, d.department_name, d1.department_name as parentName FROM departments d left join departments d1 on d.parent_department_id = d1.department_id";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Integer id = resultSet.getInt("department_id");
                        String name = resultSet.getString("department_name");
                        String parentName = resultSet.getString("parentName");
                        departments.add(new Department(id, name,
                                new Department(parentName != null ? parentName : "")
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return departments;
    }

    @Override
    public void create(Department department) {
        try (java.sql.Connection connection = Connection.getConnection()) {
            String query = department.getParentDepartment().getId() != -1
                    ? "INSERT INTO departments (name, parent_department_id) VALUES (?, ?)"
                    : "INSERT INTO departments (name) VALUES (?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, department.getName());
                if (department.getParentDepartment().getId() != -1) {
                    preparedStatement.setInt(2, department.getParentDepartment().getId());
                }
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Department added successfully.");
                } else {
                    System.out.println("Failed to add the department.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Department department) {
        try (java.sql.Connection connection = Connection.getConnection()) {
            String query = "UPDATE departments SET name = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, department.getName());
                preparedStatement.setInt(2, department.getId());
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Department information updated successfully.");
                } else {
                    System.out.println("No department found with the specified ID. No update performed.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Integer id) {
        try (java.sql.Connection connection = Connection.getConnection()) {
            String query = "DELETE FROM departments WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, id);
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Department with ID " + id + " deleted successfully.");
                } else {
                    System.out.println("No department found with ID " + id + ". No deletion performed.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Department findByName(String name) {
        Department department = null;

        try (java.sql.Connection connection = Connection.getConnection()) {
            String query = "SELECT id, name FROM departments WHERE name = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, name);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Integer departmentId = resultSet.getInt("id");
                        department = new Department(departmentId, name);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return department;
    }
}
