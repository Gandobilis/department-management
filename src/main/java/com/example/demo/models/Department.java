package com.example.demo.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Department {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty name;
    private final SimpleObjectProperty<Department> parentDepartment;

    public Department(String name) {
        this(-1, name);
    }

    public Department(Integer id, String name) {
        this(id, name, null);
    }

    public Department(String name, Department parentDepartment) {
        this(-1, name, parentDepartment);
    }

    public Department(Integer id, String name, Department parentDepartment) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.parentDepartment = new SimpleObjectProperty<>(parentDepartment);
    }

    public Integer getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public Department getParentDepartment() {
        return parentDepartment.get();
    }

    public void setParentDepartment(Department parentDepartment) {
        this.parentDepartment.set(parentDepartment);
    }

    @Override
    public String toString() {
        return getName();
    }
}
