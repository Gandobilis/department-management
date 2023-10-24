package com.example.demo.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Employee {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;

    private final SimpleObjectProperty<Department> department;

    public Employee(String fName, String lName) {
        this(-1, fName, lName, null);
    }

    public Employee(String fName, String lName, Department department) {
        this(-1, fName, lName, department);
    }

    public Employee(Integer id, String fName, String lName, Department department) {
        this.id = new SimpleIntegerProperty(id);
        this.firstName = new SimpleStringProperty(fName);
        this.lastName = new SimpleStringProperty(lName);
        this.department = new SimpleObjectProperty<>(department);
    }

    public Integer getId() {
        return id.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public Department getDepartment() {
        return department.get();
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", department=" + department +
                '}';
    }
}
