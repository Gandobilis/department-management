package com.example.demo.seeders;

import com.example.demo.database.Connection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseSeeder {
    public static void main(String[] args) {
        // Sample data for department hierarchy
        String[] headDepartments = {"HR", "IT", "Finance", "Sales"};
        String[] childDepartments = {"Recruiting", "Software Development", "Accounting", "Sales Team"};
        String[] firstNames = {"John", "Alice", "Bob", "Eva", "Michael"};
        String[] lastNames = {"Smith", "Johnson", "Doe", "Brown", "Davis"};

        try (java.sql.Connection connection = Connection.getConnection()) {
            // Insert head departments
            String departmentInsertQuery = "INSERT INTO Departments (department_name) VALUES (?)";
            try (PreparedStatement departmentStatement = connection.prepareStatement(departmentInsertQuery)) {
                for (String departmentName : headDepartments) {
                    departmentStatement.setString(1, departmentName);
                    departmentStatement.executeUpdate();
                }
            }

            // Insert child departments with parent_department_id
            String childDepartmentInsertQuery = "INSERT INTO Departments (department_name, parent_department_id) VALUES (?, ?)";
            try (PreparedStatement childDepartmentStatement = connection.prepareStatement(childDepartmentInsertQuery)) {
                int parentDepartmentId = 1; // Starting parent_department_id
                for (String departmentName : childDepartments) {
                    childDepartmentStatement.setString(1, departmentName);
                    childDepartmentStatement.setInt(2, parentDepartmentId);
                    childDepartmentStatement.executeUpdate();

                    // Increment parent_department_id for the next child department
                    parentDepartmentId++;
                    if (parentDepartmentId > 4) {
                        parentDepartmentId = 1; // Reset to the first head department
                    }
                }
            }

            // Insert employees into departments
            String employeeInsertQuery = "INSERT INTO Employees (first_name, last_name, department_id) VALUES (?, ?, ?)";
            try (PreparedStatement employeeStatement = connection.prepareStatement(employeeInsertQuery)) {
                int departmentId = 1; // Start with the first department
                for (String firstName : firstNames) {
                    for (int i = 0; i < 5; i++) {
                        employeeStatement.setString(1, firstName);
                        employeeStatement.setString(2, lastNames[i]);
                        employeeStatement.setInt(3, departmentId);
                        employeeStatement.executeUpdate();
                    }

                    // Increment departmentId and reset to 1 when it exceeds 16 (4 head + 4 child departments)
                    departmentId++;
                    if (departmentId > 16) {
                        departmentId = 1;
                    }
                }
            }

            System.out.println("Seed data inserted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
